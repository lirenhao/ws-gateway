package com.yada.web.routers

import com.yada.web.filters.AuthApiHandlerFilter
import com.yada.web.filters.AuthHandlerFilter
import com.yada.web.filters.WhitelistHandlerFilter
import com.yada.web.handlers.AuthHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
open class AuthRouterConfig @Autowired constructor(
        private val authHandler: AuthHandler,
        private val authFilter: AuthHandlerFilter,
        private val authApiFilter: AuthApiHandlerFilter,
        private val whitelistFilter: WhitelistHandlerFilter) {
    @Bean
    open fun authRouter() = router {
        "".nest {
            "/login".nest {
                GET("", authHandler::getLoginForm)
                POST("", authHandler::login)
            }
            filter(whitelistFilter)
        }

        "".nest {
            GET("/") { _ ->
                ServerResponse.ok().render("/index")
            }
            filter(whitelistFilter)
            filter { request, next -> authFilter.invoke(request, next) }
        }

        "".nest {
            GET("/logout", authHandler::logout)
            POST("/change_pwd", authHandler::changePwd)
            GET("/refresh_token", authHandler::refreshToken)
            GET("/filter_apis", authHandler::filterApis)
            filter(whitelistFilter)
            filter(authApiFilter)
        }
    }
}