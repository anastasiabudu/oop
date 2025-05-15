package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class UniversityImplementation implements OrganizationInterface {
    private String name;
    private final Map<PersonInterface, Integer> employees = new HashMap<>(); //по ключу людини знаходжу зайнятість
    private final Map<ProjectInterface, Integer> projects = new HashMap<>(); //по ключу проєкт знаходжу бюджет
    private boolean isUpdating = false;
    @Override
    public String getName() {
        return name;

    }

    @Override
    public void setName(String name) {
        this.name = name;

    }
    @Override
    public void addEmployee(PersonInterface p, int employment) {
        employees.put(p, employment);
    }

    @Override
    public Set<PersonInterface> getEmployees() {
        return employees.keySet();
    }

    @Override
    public int getEmploymentForEmployee(PersonInterface p) {
        int employment = employees.getOrDefault(p, 0);
        return employment;
    }

    @Override
    public Set<ProjectInterface> getAllProjects() {

        return projects.keySet();
    }
    @Override
    // Возвращает набор проектов, активных в указанный год
    public Set<ProjectInterface> getRunningProjects(int year) {
        Set<ProjectInterface> result = new HashSet<>();
        for (ProjectInterface p : projects.keySet()) {
            if (p.getStartingYear() <= year && p.getEndingYear() >= year) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public void registerProjectInOrganization(ProjectInterface project) {
        project.setApplicant(this);
        projects.put(project, 0);

    }

    @Override
    public int getProjectBudget(ProjectInterface pi) {
        int budget = projects.getOrDefault(pi, 0);
        return budget;
    }

    @Override
    public int getBudgetForAllProjects() { // Возвращает общий бюджет всех проектов
        int result = 0;
        for (Integer budget : projects.values()) {
            result += budget;
        }
        return result;
    }

    @Override
    public void projectBudgetUpdateNotification(ProjectInterface pi, int year, int budgetForYear) {
        if (!isUpdating) {
            try {
                isUpdating = true;
                int previousBudget = projects.getOrDefault(pi, 0); // бюджет проєкту в предыдущий год
                int totalBudget = previousBudget + budgetForYear;// новый бюджет проєкту
                projects.put(pi, totalBudget);
            } finally {
                isUpdating = false;
            }
        }
    }

}