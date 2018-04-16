package bs.devweb.projet.servlets;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

/**
 * Cette classe permet d'alleger le code en regroupant du code redondant concernant les templateResolver
 * @author Remi SCHOONAERT - Arnold BLYAU
 * @version 1.0
 */
public abstract class GenericServlet extends HttpServlet{

    protected TemplateEngine createTemplateEngine(ServletContext context){
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
        // on indique la syntaxe generale des templateResolver
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new Java8TimeDialect());

        return templateEngine;
    }

}
