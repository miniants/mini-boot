
package cn.miniants.framework.api;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**

 * ----------------------------------------
 * REST API 通用控制器
 *
 * @author 青苗
 * @since 2021-10-28
 */
public class ApiController {
    @Resource
    protected HttpServletRequest request;
    @Resource
    protected HttpServletResponse response;

}
