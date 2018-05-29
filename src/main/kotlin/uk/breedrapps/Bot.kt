/*
 * (C) Copyright 2018 Breedr Apps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.breedrapps

import com.jagrosh.jdautilities.command.CommandClient
import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.natpryce.konfig.*
import com.natpryce.konfig.ConfigurationProperties.Companion.systemProperties
import io.pokemontcg.Pokemon
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Game
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import uk.breedrapps.command.SearchCardCommand
import uk.breedrapps.command.SearchSetCommand
import uk.breedrapps.manager.StartupManager
import uk.breedrapps.manager.server
import javax.security.auth.login.LoginException
import kotlin.system.exitProcess

// TODO: Make this into some global setting
const val DEBUG = false

private val waiter: EventWaiter by lazy {
    EventWaiter()
}

val kodein = Kodein {
    bind<Pokemon>() with singleton { Pokemon() }
}


lateinit var config: Configuration

fun main(args: Array<String>) {

    val filename = if (DEBUG) "server_debug.properties" else "server.properties"

    config = systemProperties() overriding
            EnvironmentVariables() overriding
            ConfigurationProperties.fromResource(filename)

    val token = try { config[server.token] } catch (e: Misconfiguration) { exitProcess(1) }
    connect(token)

}

private fun connect(token: String) {
    try {
        JDABuilder(AccountType.BOT)
                .addEventListener(StartupManager())
                .addEventListener(waiter)
                .addEventListener(commandClient())
                .setBulkDeleteSplittingEnabled(false)
                .setToken(token)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setGame(Game.playing("Loading…"))
                .buildAsync()
    } catch (ex: LoginException) {
        ex.printStackTrace()
        exitProcess(1)
    }
}

private fun commandClient() : CommandClient {
    return CommandClientBuilder()
            .setOwnerId(config[server.ownerid])
            .setPrefix("!")
            .useDefaultGame()
            .setEmojis(
                    "\ud83d\ude0a",
                    "<:Sad:448937101380157470>",
                    "<:Sad:448937101380157470>"
            )
            .addCommands(
                    SearchCardCommand(),
                    SearchSetCommand()
            )
            .build()
}
