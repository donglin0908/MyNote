package com.coorun.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;

import com.coorun.entity.RawMessage;
import com.coorun.logcreate.impl.LogCollectionImpl;
import com.coorun.om.base.bean.UserInfo;
import com.coorun.om.base.util.Constants;

/**
 * 日志管理-AOP具体处理
 * 
 * @author DL
 * @Date: 2018/1/16
 * @Time: 15:42
 * @annotation SysLog
 */
public class SysLog {
	public void dealWithLog(JoinPoint joinPoint, Exception e) {
		// 获取-发生错误的类
		Class<? extends Object> className = joinPoint.getTarget().getClass();
		// 获取-发生错误的方法名
		String signature = joinPoint.getSignature().toString();
		String methodName = signature.substring(signature.lastIndexOf(".") + 1, signature.indexOf("("));
		// 获取-日志编号(待定-暂为方法名)
		String LogCode = methodName;
		// 获取-日志内容
		String LogContent = e.toString();
		// 获取-登陆者信息
		String userName = null;
		Object[] obj = joinPoint.getArgs();
		for (int i = 0; i < obj.length; i++) {
			if (obj[i] instanceof HttpServletRequest) {
				HttpServletRequest req = (HttpServletRequest) obj[i];
				UserInfo user = (UserInfo) req.getSession().getAttribute(Constants.CURRENT_USER);
				// 登陆者
				userName = user.getUsername();
			}
		}
		// 根目录路径
		String rootPath = getClass().getResource("/").getFile().toString();
		String[] psthList = rootPath.split("/");
		// 获取-项目名
		String projectName = null;
		for (int i = 0; i < psthList.length; i++) {
			if ("WEB-INF".equals(psthList[i])) {
				projectName = psthList[i - 1];
			}
		}
		// 生成日志
		new LogCollectionImpl().setLog(className,
				new RawMessage(LogCode, LogContent, projectName, userName, methodName));
	}
}
