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

package uk.breedrapps.command.base

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import io.pokemontcg.Pokemon
import org.kodein.di.generic.instance
import uk.breedrapps.kodein

abstract class BaseCommand : Command() {
    val api: Pokemon by kodein.instance()

    override fun execute(event: CommandEvent) {
        when(event.args) {
            null, "" -> emptyEventResult(event)
            else -> searchForTerm(event)
        }
    }

    abstract fun searchForTerm(event: CommandEvent)

    open fun emptyEventResult(event: CommandEvent) {
        event.replyWarning("Please provide a search term")
    }

}