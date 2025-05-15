package sk.stuba.fei.uim.oop.factory;

import sk.stuba.fei.uim.oop.entity.grant.*;
import sk.stuba.fei.uim.oop.entity.organization.CompanyImplementation;
import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;
import sk.stuba.fei.uim.oop.entity.organization.UniversityImplementation;
import sk.stuba.fei.uim.oop.entity.people.PersonImplementation;
import sk.stuba.fei.uim.oop.entity.people.PersonInterface;

import java.util.LinkedList;

public class DataFactory {

    public static LinkedList<AgencyInterface> getAgencies(int count) {
        LinkedList<AgencyInterface> retVal = new LinkedList<>();
        for (int i = 0; i < count; i++) {
          AgencyInterface agency = new AgencyImplementation();
          retVal.add(agency);
        }
        return retVal;
    }

    public static LinkedList<GrantInterface> getGrants(int count) {
        LinkedList<GrantInterface> retVal = new LinkedList<>();
        for (int i = 0; i < count; i++) {
          GrantInterface grant = new GrantImplementation();
          retVal.add(grant);
        }
        return retVal;
    }

    public static LinkedList<ProjectInterface> getProjects(int count) {
        LinkedList<ProjectInterface> retVal = new LinkedList<>();
        for (int i = 0; i < count; i++) {
          ProjectInterface project = new ProjectImplementation();
          retVal.add(project);
        }
        return retVal;
    }

    public static LinkedList<OrganizationInterface> getUniversities(int count) {
        LinkedList<OrganizationInterface> retVal = new LinkedList<>();
        for (int i = 0; i < count; i++) {
          OrganizationInterface org = new UniversityImplementation();
          retVal.add(org);
        }
        return retVal;
    }

    public static LinkedList<OrganizationInterface> getCompanies(int count) {
        LinkedList<OrganizationInterface> retVal = new LinkedList<>();
        for (int i = 0; i < count; i++) {
          OrganizationInterface org = new CompanyImplementation();
          retVal.add(org);
        }
        return retVal;
    }

    public static LinkedList<PersonInterface> getPersons(int count) {
        LinkedList<PersonInterface> retVal = new LinkedList<>();
        for (int i = 0; i < count; i++) {
          PersonInterface person = new PersonImplementation();
          retVal.add(person);
        }
        return retVal;
    }
}
