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

package uk.breedrapps.command

import com.jagrosh.jdautilities.command.CommandEvent
import io.pokemontcg.model.CardSet
import io.reactivex.Observable
import net.dv8tion.jda.core.EmbedBuilder
import uk.breedrapps.command.base.BaseCommand
import uk.breedrapps.util.basicDescription
import uk.breedrapps.util.fullDescription
import java.lang.StringBuilder

class SearchSetCommand : BaseCommand() {

    init {
        name = "set"
        help = "Searches TCG database for a given set"
        guildOnly = false
        cooldown = 10
    }

    override fun searchForTerm(event: CommandEvent) {
        when(event.args) {
            null, "" -> emptyEventResult(event)
            "std", "standard" -> searchForLegalSet(event, Format.Standard)
            "exp", "expanded" -> searchForLegalSet(event, Format.Expanded)
            else -> searchForSet(event)
        }
    }

    override fun emptyEventResult(event: CommandEvent) {
        resultsForSet(event, api.set().observeAll())
    }

    private fun resultsForSet(event: CommandEvent, request: Observable<List<CardSet>>, fullSetInfo: Boolean = false) {

        request.flatMapIterable { it }
                .groupBy { it.series }
                .subscribe ({
                    observable -> observable
                        .filter { !it.ptcgoCode.isNullOrEmpty() }
                        .toList()
                        .filter { !it.isEmpty() }
                        .subscribe { result -> printSetList(event, observable.key!!, result, fullSetInfo) }
                }, {
                    it.printStackTrace()
                    event.replyError("Something went wrong?!")
                })
    }

    private fun printSetList(event: CommandEvent, series: String, sets: List<CardSet>, fullSetInfo: Boolean = false) {
        val builder = StringBuilder("Found ${sets.size} set(s)\n\n")

        sets.forEach {
            val setDescription = when(it.ptcgoCode){
                null, "" -> ""
                else -> if (fullSetInfo) {
                    it.fullDescription.plus("\n\n")
                } else {
                    it.basicDescription.plus("\n\n")
                }
            }
            builder.append(setDescription)
        }

        event.replyInDm(EmbedBuilder().setTitle("Search Results ($series)").setDescription(builder).build())
    }

    private fun searchForLegalSet(event: CommandEvent, format: Format) {
        when(format) {
            is Format.Standard -> resultsForSet(event, api.set().where{ standardLegal = true }.observeAll(), false)
            is Format.Expanded -> resultsForSet(event, api.set().where{ expandedLegal = true }.observeAll(), false)
        }
    }

    private fun searchForSet(event: CommandEvent) {

        val query = event.args

        if(query.length < 4) {
            event.replyWarning("Please enter a longer search term")
            return
        }

        resultsForSet(event, api.set().where { name = query }.observeAll(), fullSetInfo = true)
    }

    private sealed class Format {
        object Standard: Format()
        object Expanded: Format()
    }

}