package sk.stuba.fei.uim.oop.entity.grant;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;
import sk.stuba.fei.uim.oop.utility.Constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProjectImplementation implements ProjectInterface {

    private String projectName;
    private int  startingYear;

    private final Map<Integer,Integer> budgetForYear= new HashMap<>();
    private int totalBudget;
    private final Set<PersonInterface> participants = new HashSet<>();

    private OrganizationInterface applicant;

    @Override
    public String getProjectName() {
        return projectName;
    }

    @Override
    public void setProjectName(String name) {
        this.projectName = name;

    }


    @Override
    public int getStartingYear() {
        return startingYear;

    }

    @Override
    public void setStartingYear(int year) {
        this.startingYear = year;

        for(int i= startingYear; i <= startingYear + Constants.PROJECT_DURATION_IN_YEARS - 1; i++){
            budgetForYear.put(i,0);

        }
    }

    @Override
    public int getEndingYear() {
        int endingYear = startingYear + Constants.PROJECT_DURATION_IN_YEARS - 1;
        return endingYear;

    }

    @Override
    public int getBudgetForYear(int year) {
        // Использует метод getOrDefault, который возвращает значение, связанное с ключом (годом),
        // или значение по умолчанию (0) в случае, если для указанного ключа (года) значение не было найдено в map.
        return this.budgetForYear.getOrDefault(year, 0);
    }


    @Override
    public void setBudgetForYear(int year, int budget) {
        int duration = Constants.PROJECT_DURATION_IN_YEARS;
        int annualBudget = budget / duration; // равномерное расспределение бюджета за год

        // Установка расчетного бюджета на каждый год длительности проекта.
        for (int i = startingYear; i <= startingYear + duration - 1; i++) {
            budgetForYear.put(i, annualBudget); // Установка годового бюджета для каждого года начиная с startingYear
        }
        totalBudget = budget;

        if (applicant != null) { // Нотификация о изменении бюджета
            applicant.projectBudgetUpdateNotification(this, year, budget);
        }
    }



    @Override
    public int getTotalBudget() {
        return totalBudget;
    }

    @Override
    public void addParticipant(PersonInterface participant) {
        participants.add(participant); // Добавление участника проекта
    }

    @Override
    public Set<PersonInterface> getAllParticipants() {
        return participants;
    }

    @Override
    public OrganizationInterface getApplicant() {
        return applicant; // Возвращает организацию-заявителя
    }

    @Override
    public void setApplicant(OrganizationInterface applicant) {
        this.applicant = applicant; // Установка организации-заявителя
    }
}
