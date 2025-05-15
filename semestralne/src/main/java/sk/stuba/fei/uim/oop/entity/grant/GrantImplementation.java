package sk.stuba.fei.uim.oop.entity.grant;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.*;
import java.time.Year;


import static sk.stuba.fei.uim.oop.utility.Constants.MAX_EMPLOYMENT_PER_AGENCY;

public class GrantImplementation implements GrantInterface {

    private String identifier;
    private AgencyInterface agency;
    private int year;
    private int budget;
    private GrantState state;
    private Map<ProjectInterface, Integer> projects = new LinkedHashMap<>();//порядок добавления элементов сохраняется

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;

    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
        this.year = year;

    }

    @Override
    public AgencyInterface getAgency() {
        return agency;
    }

    @Override
    public void setAgency(AgencyInterface agency) {
        this.agency = agency;

    }

    @Override
    public int getBudget() {
        return budget;
    }

    @Override
    public void setBudget(int budget) {
        this.budget = budget;

    }

    @Override
    public int getRemainingBudget() { //скільки бюжету залишилося
        int remainingBudget = budget;
        // Перебор всех проектов, зарегистрированных в гранте
        for (ProjectInterface project : projects.keySet()) {
            int projectBudget = project.getTotalBudget(); // Получение общего бюджета каждого проекта
            remainingBudget -= projectBudget;//оставшийся бюджет
        }

        return remainingBudget;
    }

    @Override
    public int getBudgetForProject(ProjectInterface project) {
        return budget;
    }


    @Override
    public boolean registerProject(ProjectInterface project) {
        if (!GrantState.STARTED.equals(this.state)) {
            return false;
        }
        if (this.year != project.getStartingYear()) {
            return false;
        }
        if (project.getAllParticipants().isEmpty()) {
            return false;
        }
        OrganizationInterface applicant = project.getApplicant();
        if (applicant == null) {
            return false;// Если организация-заявитель не указана, регистрация проекта невозможна
        }
        for (PersonInterface person : project.getAllParticipants()) { //  все участники проекта являются сотрудниками заявителя
            if (!applicant.getEmployees().contains(person)) {
                return false;
            }
        }
        projects.put(project, 0); // Регистрация проекта с начальным бюджетом 0
        return true;
    }


    @Override
    public Set<ProjectInterface> getRegisteredProjects() {
        return projects.keySet(); // Возвращает зарегистрированные проекты

    }

    @Override
    public GrantState getState() {
        return state;
    }

    @Override
    public void callForProjects() {
        this.state = GrantState.STARTED;

    }


    private boolean evaluateParticipantOverEmployment(PersonInterface person, ProjectInterface grantproject, Set<ProjectInterface> agencyprojects) {
        int capacity = 0;
        for (OrganizationInterface org : person.getEmployers()) {  // Перебор всех организаций, в которых работает участник
            for (ProjectInterface project : org.getAllProjects()) {  // Перебор всех проектов данной организации
                if (project.getAllParticipants().contains(person) &&
                        ((project.getStartingYear() <= getYear() && project.getEndingYear() >= getYear()
                                && agencyprojects.contains(project))
                                || (this.getRegisteredProjects().contains(project) && grantproject.equals(project)))) {
                    capacity += org.getEmploymentForEmployee(person);

                }
            }
        }
        // Проверяем, превышает ли суммарное количество часов лимит в какой-либо из годов
        if (capacity > MAX_EMPLOYMENT_PER_AGENCY) {
            System.out.println("Overemployment detected for " + person.getName());
            return false;
        }
        return true;
    }

@Override
    public void evaluateProjects() {
        if (!GrantState.STARTED.equals(state)) {
            System.out.println("The grant state is not STARTED. Exiting evaluation.");
            return;
        }

        Set<ProjectInterface> agencyprojects = new HashSet<>(); // Создание множества для хранения всех проектов, зарегистрированных в агентстве
        for (GrantInterface grant : agency.getAllGrants()) { // Перебор всех грантов агентства
            for (ProjectInterface project : grant.getRegisteredProjects()) { // Перебор всех проектов гранта
                if (project.getTotalBudget() > 0) {
                    agencyprojects.add(project);

                }
            }
        }


        Map<ProjectInterface, Boolean> eligibilityMap = new HashMap<>(); // карты для отслеживания соответствия проектов критериям гранта
        for (ProjectInterface project : projects.keySet()) {// Перебор всех зарегистрированных проектов гранта
            boolean isEligible = true;
            // Проверка каждого участника проекта на переработку.
            for (PersonInterface person : project.getAllParticipants()) {
                if (!evaluateParticipantOverEmployment(person, project, agencyprojects)) {
                    isEligible = false;
                    break;
                }
            }
            eligibilityMap.put(project, isEligible);
            System.out.println("Project " + project.getProjectName() + (isEligible ? " has passed" : " has not passed") + " the evaluation.");
        }

        this.state = GrantState.EVALUATING;
        closeGrant();

        distributeBudget(eligibilityMap);

    }


    private void distributeBudget(Map<ProjectInterface, Boolean> eligibilityMap) {
        // Сбор списка проектов, которые прошли проверку и соответствуют критериям гранта
        List<ProjectInterface> eligibleProjects = new ArrayList<>();
        for (ProjectInterface project : projects.keySet()) {
            if (Boolean.TRUE.equals(eligibilityMap.get(project))) {
                eligibleProjects.add(project);
            }
        }

        int projectsToFund = eligibleProjects.size(); // Определение количества проектов, которым будет выделен бюджет

        if (projectsToFund > 0) { //Распределение бюджета между проектами, прошедшими проверку
            int budgetPerProject;
            if (projectsToFund == 1) {
                // Если один проект прошел, весь бюджет отходит этому проекту
                budgetPerProject = budget;
                eligibleProjects.get(0).setBudgetForYear(eligibleProjects.get(0).getStartingYear(), budgetPerProject);
            } else {
                // Вычисляем количество проектов, которым будет выделен бюджет (половина от прошедших)
                projectsToFund = projectsToFund / 2;
                budgetPerProject = budget / projectsToFund;

                // Распределение бюджета среди первых половины зарегистрированных проектов
                for (int i = 0; i < projectsToFund; i++) {
                    ProjectInterface project = eligibleProjects.get(i);
                    project.setBudgetForYear(project.getStartingYear(), budgetPerProject);
                }
            }
        }

        // Устанавливаем бюджет в 0 для оставшихся проектов
        for (ProjectInterface project : projects.keySet()) {
            if (!eligibleProjects.contains(project) || eligibleProjects.indexOf(project) >= projectsToFund) {
                project.setBudgetForYear(project.getStartingYear(), 0);
            }
        }
    }


    @Override
    public void closeGrant() {
        this.state = GrantState.CLOSED;

    }
}
