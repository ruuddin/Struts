package alm.actions;

import com.opensymphony.xwork2.ActionSupport;

public class ListAction extends ActionSupport{

    private static final long serialVersionUID = 1L;
    
    public String view(){
        System.out.println("Running ListAction.view()");
        
        return "Success";
    }

}
