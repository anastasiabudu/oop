package sk.stuba.fei.uim.oop.entity.organization;

import sk.stuba.fei.uim.oop.entity.grant.ProjectInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static sk.stuba.fei.uim.oop.utility.Constants.COMPANY_INIT_OWN_RESOURCES;

public class CompanyImplementation implements OrganizationInterface {

    private String name;
    private boolean isUpdating = false;

    private final Map<PersonInterface, Integer> employees = new HashMap<>(); //по ключу людини знаходжу зайнятість
    private final Map<ProjectInterface, Integer> projects = new HashMap<>(); //по ключу проєкт знаходжу бюджет

    private int ownResources = COMPANY_INIT_OWN_RESOURCES;

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
        return employees.keySet(); // возвращает множество людей
    }

    @Override
    public int getEmploymentForEmployee(PersonInterface p) {
        int employment = employees.getOrDefault(p, 0); //
        return employment;
    }

    @Override
    public Set<ProjectInterface> getAllProjects() {
        return projects.keySet(); // возвращает множество проєктов
    }

    @Override
    public Set<ProjectInterface> getRunningProjects(int year) {
        Set<ProjectInterface> result = new HashSet<>();  // Инициализация множества для хранения активных проектов
        for (ProjectInterface p : projects.keySet()) {
            if (p.getStartingYear() <= year && p.getEndingYear() >= year) { // Проверка, активен ли проект в заданном году
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public void registerProjectInOrganization(ProjectInterface project) {
        project.setApplicant(this); // Установка текущей организации в качестве заявителя проекта
        projects.put(project, 0);

    }

    @Override
    public int getProjectBudget(ProjectInterface pi) {
        //Если проект `pi` зарегистрирован в карте `projects`,
        // возвращается его бюджет. Если проект не найден, возвращается 0.
        int budget = projects.getOrDefault(pi, 0);
        return budget;
    }

    @Override
    public int getBudgetForAllProjects() {
        int result = 0;
        for (Integer budget : projects.values()) {
            result += budget;
        }

        return result;
    }




    @Override
    public void projectBudgetUpdateNotification(ProjectInterface pi, int year, int budgetForYear) {
        if (!isUpdating) {     // Проверка, не происходит ли уже обновление (предотвращение рекурсивного вызова или конкурентного доступа)
            try {
                isUpdating = true;

                // Логируем начало операции
                System.out.println("Updating budget for project: " + pi.getProjectName() + " for year: " + year);

                // Получаем и обновляем текущий бюджет проекта
                int currentBudget = projects.getOrDefault(pi, 0);
                int newBudget = currentBudget + budgetForYear;
                projects.put(pi, newBudget);
                System.out.println("Added grant funds: " + budgetForYear + ". New total budget: " + newBudget);

                // Проверяем возможность удвоения финансирования из собственных ресурсов
                if (ownResources >= budgetForYear) {
                    projects.put(pi, newBudget + budgetForYear);
                    ownResources -= budgetForYear;
                    System.out.println("Company resources used to double the funding: " + budgetForYear + ". Remaining company resources: " + ownResources);
                } else if(ownResources > 0) {
                    projects.put(pi, budgetForYear + ownResources);
                    ownResources-= ownResources;
                    System.out.println("Company resources used to double the funding: " + budgetForYear + ". Remaining company resources: " + ownResources);
                }
                else{
                    System.out.println("Insufficient company resources to double the funding.");
                }

            } finally {
                isUpdating = false;
            }
        }
    }

}