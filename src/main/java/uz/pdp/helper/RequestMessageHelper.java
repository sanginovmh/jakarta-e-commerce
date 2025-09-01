package uz.pdp.helper;

import jakarta.servlet.http.HttpServletRequest;

public class RequestMessageHelper {
    private HttpServletRequest req;

    public RequestMessageHelper(HttpServletRequest req) {
        this.req = req;
    }

    public void assignSessionMsgToReq() {
        String msg = (String) req.getSession().getAttribute("message");
        if (msg != null) {
            req.setAttribute("message", msg);
            req.getSession().removeAttribute("message");
        }
    }
}
