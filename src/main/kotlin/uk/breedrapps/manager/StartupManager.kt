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

package uk.breedrapps.manager

import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.events.ExceptionEvent
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.events.ReconnectedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import uk.breedrapps.DEBUG

class StartupManager : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
       printAppInfo(event)
    }

    override fun onReconnect(event: ReconnectedEvent) {
        printAppInfo(event)
    }

    override fun onException(event: ExceptionEvent) {
        event.cause.printStackTrace()
    }

    private fun printAppInfo(event: Event) {
        val jda = event.jda
        val selfUser = jda.selfUser
        println("""
        ||-========================================================
        || ${if (DEBUG) "******DEBUG BUILD******" else "TCGenuis-Bot"}
        || Account Info: ${selfUser.name}#${selfUser.discriminator} (ID: ${selfUser.id})
        || Connected to ${jda.guilds.size} guilds, ${jda.textChannels.size} text channels
        || Status: ${jda.status}
        ||-========================================================
        """.trimMargin("|"))

        jda.guilds.forEachIndexed { index, guild ->
            println("[$index] Guild joined: ${guild.idLong} ${guild.name} (${guild.regionRaw})")
        }

    }

}