package sk.stuba.fei.uim.oop.phone;

import sk.stuba.fei.uim.oop.application.Application;

import java.util.Set;

import java.util.HashSet;

public class Phone {
    private State currentState;
    private Set<Application> installedApps;
    private String password = "123";

    public Phone() {
        installedApps = new HashSet<>();
        installedApps.add(new Application("AppStore", true, false));
        installedApps.add(new Application("Firefox", false, false));
        installedApps.add(new Application("Camera", false, true));
        currentState = new State.LockedState(this);
    }

    void setState(State newState) {
        currentState = newState;
    }

    public void powerButtonPressed() {
        currentState.powerButtonPressed();
    }

    public void backButtonPressed() {
        currentState.backButtonPressed();
    }

    public void install(Application app) {
        currentState.install(app);
    }

    void uninstallApplication(Application app) {
        installedApps.remove(app);
    }

    void uninstall(Application app) {
        currentState.uninstall(app);
    }

    public void start(Application app) {
        currentState.start(app);
    }

    public void passwordEntered(String password) {
        currentState.passwordEntered(password);
    }

    public Set<Application> getInstalledApplications() {
        return installedApps;
    }

    public Application getRunningApplication() {
        if (currentState instanceof State.ApplicationRunningState) {
            return ((State.ApplicationRunningState) currentState).runningApp;
        }
        return null;
    }

    public String getStateName() {
        return currentState.getName();
    }

    public String getPassword() {
        return password;
    }

    void installApplication(Application app) {
        installedApps.add(app);
    }
}
