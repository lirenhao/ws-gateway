package com.yada.web.routers

import com.yada.web.filters.AuthAdminApiHandlerFilter
import com.yada.web.filters.WhitelistHandlerFilter
import com.yada.web.handlers.apis.OrgHandler
import com.yada.web.handlers.apis.RoleHandler
import com.yada.web.handlers.apis.SvcHandler
import com.yada.web.handlers.apis.UserHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
open class AdminApiRouterConfig @Autowired constructor(
        private val roleHandler: RoleHandler,
        private val orgHandler: OrgHandler,
        private val svcHandler: SvcHandler,
        private val userHandler: UserHandler,
        private val authAdminApiFilter: AuthAdminApiHandlerFilter,
        private val whitelistFilter: WhitelistHandlerFilter) {
    @Bean
    open fun adminApiRouter() = router {
        "/admin/apis".nest {
            "/role".nest {
                GET("", roleHandler::getAll)
                GET("/{id}", roleHandler::get)
                GET("/{id}/exist", roleHandler::exist)
                PUT("", roleHandler::createOrUpdate)
                DELETE("/{id}", roleHandler::delete)
            }
            "/org".nest {
                GET("", orgHandler::getTree)
                GET("/{id}", orgHandler::get)
                GET("/{id}/exist", orgHandler::exist)
                PUT("", orgHandler::createOrUpdate)
                DELETE("/{id}", orgHandler::delete)
            }
            "/svc".nest {
                GET("", svcHandler::getAll)
                GET("/{id}", svcHandler::get)
                PUT("", svcHandler::createOrUpdate)
                DELETE("", svcHandler::delete)
            }
            "/user".nest {
                GET("", userHandler::getUsersBy)
                GET("/{id}", userHandler::get)
                GET("/{id}/exist", userHandler::exist)
                PUT("", userHandler::createOrUpdate)
                DELETE("/{id}", userHandler::delete)
            }
        }
        filter(whitelistFilter)
        filter(authAdminApiFilter)
    }
}