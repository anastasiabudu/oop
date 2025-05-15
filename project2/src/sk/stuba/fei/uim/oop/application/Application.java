package sk.stuba.fei.uim.oop.application;

public class Application {
    private String name;
    private boolean canInstall;
    private boolean startableFromLocked;

    public Application(String name, boolean canInstallApplication, boolean startableFromLockedScreen) {
        this.name = name;
        this.canInstall = canInstallApplication;
        this.startableFromLocked = startableFromLockedScreen;
    }

    public Application(String name) {
        this(name, false, false);
    }

    public String getName() {
        return name;
    }

    public boolean canInstallApplication() {
        return canInstall;
    }

    public boolean startableFromLockedScreen() {
        return startableFromLocked;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Application other = (Application) obj;
        return name.equals(other.name) && canInstall == other.canInstall && startableFromLocked == other.startableFromLocked;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (canInstall ? 1 : 0);
        result = 31 * result + (startableFromLocked ? 1 : 0);
        return result;
    }
}
