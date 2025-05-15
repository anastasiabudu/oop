package sk.stuba.fei.uim.oop.entity.people;

import sk.stuba.fei.uim.oop.entity.organization.OrganizationInterface;

import java.util.HashSet;
import java.util.Set;

public class PersonImplementation implements PersonInterface {

    private String name;
    private String address;
    private Set<OrganizationInterface> employers = new HashSet<>();
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String getAddress(){

        return address;
    }
    @Override
    public void setAddress(String address){
        this.address = address;
    }
    @Override
    public void addEmployer(OrganizationInterface organization) {     // Добавляет организацию в множество организаций, где человек является сотрудником

        boolean added = employers.add(organization);
        if (added) {
            System.out.println("Added employer " + organization.getName() + " to " + this.getName());
        }
    }

    @Override
    public Set<OrganizationInterface> getEmployers() // Возвращает множество организаций, где человек является сотрудником
        {
            return employers;
        }

}
