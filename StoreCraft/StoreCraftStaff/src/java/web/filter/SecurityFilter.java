package web.filter;

import entity.StaffEntity;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.enumeration.StaffTypeEnum;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})

public class SecurityFilter implements Filter {

    FilterConfig filterConfig;

    private static final String CONTEXT_ROOT = "/StoreCraftStaff";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();

        if (httpSession.getAttribute("isLogin") == null) {
            httpSession.setAttribute("isLogin", false);
        }

        Boolean isLogin = (Boolean) httpSession.getAttribute("isLogin");

        if (!excludeLoginCheck(requestServletPath)) {
            if (isLogin == true) {
                StaffEntity currentStaffEntity = (StaffEntity) httpSession.getAttribute("currentStaffEntity");

                if (checkAccessRight(requestServletPath, currentStaffEntity.getStaffTypeEnum())) {
                    chain.doFilter(request, response);
                } else {
                    httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
                }
            } else {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {

    }

    private Boolean checkAccessRight(String path, StaffTypeEnum staffType) {
        if (staffType.equals(StaffTypeEnum.REGULAR)) {
            if (path.equals("/systemAdministration/createDiscountCode.xhtml")
                    || path.equals("/systemAdministration/customerDetails.xhtml")
                    || path.equals("/systemAdministration/customerManagement.xhtml")
                    || path.equals("/dashboard.xhtml")
                    || path.equals("/systemAdministration/discountCodeManagement.xhtml")
                    || path.equals("/systemAdministration/filterProductsByCategory.xhtml")
                    || path.equals("/systemAdministration/productManagement.xhtml")
                    || path.equals("/systemAdministration/reviewManagement.xhtml")
                    || path.equals("/systemAdministration/reviewChain.xhtml")
                    || path.equals("/systemAdministration/salesManagement.xhtml")
                    || path.equals("/systemAdministration/viewProductDetails.xhtml")
                    || path.equals("/systemAdministration/viewUpdateDiscountCode.xhtml")
                    || path.equals("/gameFeatures/communityGoals.xhtml")
                    || path.equals("/gameFeatures/scavengerHunt.xhtml")) {
                return true;
            } else {
                return false;
            }
        } else if (staffType.equals(StaffTypeEnum.MANAGER)) {
            if (path.equals("/systemAdministration/createDiscountCode.xhtml")
                    || path.equals("/systemAdministration/customerDetails.xhtml")
                    || path.equals("/systemAdministration/customerManagement.xhtml")
                    || path.equals("/dashboard.xhtml")
                    || path.equals("/systemAdministration/discountCodeManagement.xhtml")
                    || path.equals("/systemAdministration/filterProductsByCategory.xhtml")
                    || path.equals("/systemAdministration/productManagement.xhtml")
                    || path.equals("/systemAdministration/reviewManagement.xhtml")
                    || path.equals("/systemAdministration/reviewChain.xhtml")
                    || path.equals("/systemAdministration/salesManagement.xhtml")
                    || path.equals("/systemAdministration/staffManagement.xhtml")
                    || path.equals("/systemAdministration/viewProductDetails.xhtml")
                    || path.equals("/systemAdministration/viewUpdateDiscountCode.xhtml")
                    || path.equals("/gameFeatures/communityGoals.xhtml")
                    || path.equals("/gameFeatures/scavengerHunt.xhtml")) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private Boolean excludeLoginCheck(String path) {
        if (path.equals("/index.xhtml")
                || path.equals("/accessRightError.xhtml")
                || path.startsWith("/javax.faces.resource")) {
            return true;
        } else {
            return false;
        }
    }
}
