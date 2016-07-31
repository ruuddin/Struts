
import java.util.Hashtable;

public class TestCaseDetails {

    private String testCaseName;
    private String enable;
    // private String description;

    String actionName[];
    Hashtable<String, String> actionInputs[];

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public void setEnableValue(String enable) {
        this.enable = enable;
    }

    /* public void setDescription(String description) {
         this.description = description;
     }*/

    public String getTestCaseName() {
        return this.testCaseName;
    }

    public String getEnableValue() {
        return this.enable;
    }

    /*
        public String getDescription() {
            return this.description;
        }*/

    public String[] getActionNames() {
        return this.actionName;
    }

    public void setActionNames(String actionsArray[]) {
        this.actionName = actionsArray;
    }

    public Hashtable<String, String>[] getActionInputs() {
        return this.actionInputs;
    }

    public void setActionInputs(Hashtable<String, String> actionInputs[]) {
        this.actionInputs = actionInputs;
    }

}
