package com.qinshiji.proxy;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


/**
 * @author qinshiji
 * @date 2019/11/21 14:09
 */
public class ProxyUtil {
    /**
     * 获得代理实例
     *
     * @param target 目标对象
     * @return
     */
    public static Object getProxyInstance(Object target) {
        Object proxy = null;
        Class targetInterface = target.getClass().getInterfaces()[0];
        String line = "\n";
        String tab = "\t";
        String packageContext = "package com.qinshiji;";
        String importContext = line+"import com.qinshiji.dao.UserDao;";
        String classContext = line + "public class $Proxy implements " + targetInterface.getSimpleName() + "{" + line;
        String targetContext = "private " + targetInterface.getSimpleName() + " dao;" + line;
        String constructorContext = "public $Proxy(" + targetInterface.getSimpleName() + " dao) { this.dao = dao; }" + line;
        String methodContext = "";
        Method[] methods = targetInterface.getDeclaredMethods();
        for (Method method : methods) {
            Class<?> returnType = method.getReturnType();
            Class<?>[] parameterTypes = method.getParameterTypes();
            String parameters = "";
            String targetParameters = "";
            if (parameterTypes.length > 0) {
                int flag = 0;
                for (Class<?> parameterType : parameterTypes) {
                    parameters += parameterType.getSimpleName() + " p" + flag + ",";
                    targetParameters += " p" + flag + ",";
                    flag++;
                }
                parameters = parameters.substring(0, parameters.lastIndexOf(",") - 1);
                targetParameters = targetParameters.substring(0, targetParameters.lastIndexOf(",") - 1);
            }
            methodContext += "public " + returnType.getSimpleName() + " " + method.getName() + "(" + parameters + ") {" + line
                    + tab + tab + "System.out.println(\"proxy\");" + line
                    + tab + tab;
            if (!"void".equals(returnType.getSimpleName())) {
                methodContext += "return ";
            }
            methodContext += "dao." + method.getName() + "(" + targetParameters + ");" + line;
            methodContext += tab + "}" ;
        }
        methodContext+= line + "}";
        String proxyClassContext = packageContext+importContext + classContext + targetContext + constructorContext + methodContext;
        File file = new File("c:\\com\\qinshiji\\$Proxy.java");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(proxyClassContext);
            fileWriter.close();

            //        动态编译java文件
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            //获取java文件管理类
            StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
            //获取java文件对象迭代器
            Iterable<? extends JavaFileObject> it = manager.getJavaFileObjects(file);
            //获取编译任务
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, it);
            //执行编译任务
            task.call();
            manager.close();
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("file:c:\\\\")});
            Class<?> aClass = urlClassLoader.loadClass("com.qinshiji.$Proxy");
            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(targetInterface);
            proxy = declaredConstructor.newInstance(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proxy;
    }
}
