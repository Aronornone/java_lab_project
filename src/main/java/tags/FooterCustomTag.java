package tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class FooterCustomTag extends SimpleTagSupport {

    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        out.println("<div class=\"footer\"> <hr class=\"headerLine\">");
        out.println("<p>Production by: LabOfFour, 2017.");
        out.println("No rights reserved.</p></div>");
    }
}


