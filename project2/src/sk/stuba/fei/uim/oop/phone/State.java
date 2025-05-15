package sk.stuba.fei.uim.oop.phone;

import sk.stuba.fei.uim.oop.application.Application;

abstract class State {
    String getName() {
        return getClass().getSimpleName().replace("State", "");
    }

    protected Phone phone;

    public State(Phone phone) {
        this.phone = phone;
    }

    public abstract void powerButtonPressed();
    public abstract void backButtonPressed();
    public abstract void install(Application app);
    public abstract void uninstall(Application app);
    public abstract void start(Application app);
    public abstract void passwordEntered(String password);

    static class LockedState extends State {
        public LockedState(Phone phone) {
            super(phone);
        }

        @Override
        public void powerButtonPressed() {
            phone.setState(new WaitingForPasswordState(phone));
        }

        @Override
        public void backButtonPressed() {}

        @Override
        public void install(Application app) {}

        @Override
        public void uninstall(Application app) {}

        @Override
        public void start(Application app) {}

        @Override
        public void passwordEntered(String password) {}
    }

    static class WaitingForPasswordState extends State {
        public WaitingForPasswordState(Phone phone) {
            super(phone);
        }

        @Override
        public void powerButtonPressed() {
            phone.setState(new LockedState(phone));
        }

        @Override
        public void backButtonPressed() {}

        @Override
        public void install(Application app) {}

        @Override
        public void uninstall(Application app) {}

        @Override
        public void start(Application app) {}

        @Override
        public void passwordEntered(String password) {
            if (phone.getPassword().equals(password)) {
                phone.setState(new HomescreenState(phone));
            }
        }
    }

    static class HomescreenState extends State {
        public HomescreenState(Phone phone) {
            super(phone);
        }

        @Override
        public void powerButtonPressed() {
            phone.setState(new LockedState(phone));
        }

        @Override
        public void backButtonPressed() {}

        @Override
        public void install(Application app) {
            if (app.canInstallApplication()) {
                phone.installApplication(app);
            }
        }

        @Override
        public void uninstall(Application app) {
            phone.uninstallApplication(app);
        }

        @Override
        public void start(Application app) {
            if (app.startableFromLockedScreen() || phone.getInstalledApplications().contains(app)) {
                phone.setState(new ApplicationRunningState(phone, app));
            }
        }

        @Override
        public void passwordEntered(String password) {}
    }

    static class ApplicationRunningState extends State {
        Application runningApp;

        public ApplicationRunningState(Phone phone, Application app) {
            super(phone);
            runningApp = app;
        }

        @Override
        public void powerButtonPressed() {
            phone.setState(new LockedState(phone));
        }

        @Override
        public void backButtonPressed() {
            phone.setState(new HomescreenState(phone));
        }

        @Override
        public void install(Application app) {
            if(phone.getRunningApplication().canInstallApplication()) {
                phone.installApplication(app);
            }

        }

        @Override
        public void uninstall(Application app) {
            if (app != runningApp) {
                phone.uninstallApplication(app);
            }
        }

        @Override
        public void start(Application app) {}

        @Override
        public void passwordEntered(String password) {}
    }


}






