package sk.stuba.fei.uim.oop.entity.grant;

import java.util.*;

// Класс, реализующий интерфейс AgencyInterface для управления грантами
public class AgencyImplementation implements AgencyInterface{

    private String name; // Название агентства
    private final Set<GrantInterface> grants = new HashSet<>(); // Множество грантов, предоставляемых агентством

    @Override
    public String getName() {
        return name; // Возвращает название агентства
    }

    @Override
    public void setName(String name) {
        this.name = name; // Устанавливает название агентства
    }

    @Override
    public void addGrant(GrantInterface gi, int year) {
        gi.setYear(year); // Устанавливает год для гранта
        grants.add(gi); // Добавляет грант в множество грантов
    }

    @Override
    public Set<GrantInterface> getAllGrants() {
        return grants; // Возвращает множество всех грантов, управляемых агентством
    }

    @Override
    public Set<GrantInterface> getGrantsIssuedInYear(int year) { // Возвращает множество грантов, выданных в указанный год
        Set<GrantInterface> result = new HashSet<>();
        for (GrantInterface gi : grants) {
            if (gi.getYear() == year) { // Проверяет, соответствует ли год гранта указанному году
                result.add(gi); // Добавляет грант в результат
            }
        }
        return result;
    }
}
