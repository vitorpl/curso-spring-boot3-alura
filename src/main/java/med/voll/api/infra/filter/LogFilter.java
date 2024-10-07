package med.voll.api.infra.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

//@WebFilter(urlPatterns = "/api/**")
@Component
@WebFilter(urlPatterns = "/login/**")
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Requisição recebida em: " + LocalDateTime.now());
        filterChain.doFilter(servletRequest, servletResponse);
    }

}

/**
 * 
 * Filter é um dos recursos que fazem parte da especificação de Servlets, a qual
 * padroniza o tratamento de requisições e respostas em aplicações Web no Java.
 * Ou seja, tal recurso não é específico do Spring, podendo assim ser utilizado
 * em qualquer aplicação Java.
 * 
 * É um recurso muito útil para isolar códigos de infraestrutura da aplicação,
 * como, por exemplo, segurança, logs e auditoria, para que tais códigos não
 * sejam duplicados e misturados aos códigos relacionados às regras de negócio
 * da aplicação.
 * 
 * O método doFilter é chamado pelo servidor automaticamente, sempre que esse
 * filter tiver que ser executado, e a chamada ao método filterChain.doFilter
 * indica que os próximos filters, caso existam outros, podem ser executados. A
 * anotação @WebFilter, adicionada na classe, indica ao servidor em quais
 * requisições esse filter deve ser chamado, baseando-se na URL da requisição.
 * 
 */