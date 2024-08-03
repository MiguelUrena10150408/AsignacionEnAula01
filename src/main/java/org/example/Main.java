package org.example;
import io.javalin.Javalin;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
import io.javalin.http.BadRequestResponse;
import io.javalin.http.staticfiles.Location;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public enum Constantes{
        USER
    }
    public static void main(String[] args) {
        var app = Javalin.create(javalinConfig -> {
            javalinConfig.staticFiles.add("/publico/", Location.CLASSPATH);
                })
                .before(ctx -> {
                    AtomicBoolean estado = new AtomicBoolean(false);
                    List<String> urly = Arrays.asList("/login.html", "/autenticar");
                    urly.forEach(s -> {
                        if(s.equalsIgnoreCase(ctx.path())){ estado.set(true);}
                    });

                   User usuario =  ctx.sessionAttribute(Constantes.USER.name());

                   if(usuario == null && !estado.get()){
                    //throw new UnauthorizedResponse("Sin Autorizacion");
                       ctx.redirect("/login.html");
                   }
                })
                .get("/", ctx -> ctx.result("Bienvenidos a Nuestra Pagina Web"))

                .post("/autenticar", ctx ->{
                    //Valores del Formulario
                    String user = ctx.formParam("usuario");
                    String namer = ctx.formParam("Nombre");
                    var password = ctx.formParam("passkey");

                    if (user == null || password == null){
                        throw new BadRequestResponse("No se ha enviado la informacion de inicio de sesion");
                    }
                    //
                    ctx.sessionAttribute(Constantes.USER.name(), new User(user, password, namer));
                    //
                    ctx.redirect("/");
                })
                .start(7070);
        }

        public record User(String username, String Contraseña, String nombre){

        }
    }
