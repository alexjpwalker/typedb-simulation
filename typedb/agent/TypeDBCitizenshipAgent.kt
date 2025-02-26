/*
 * Copyright (C) 2022 Vaticle
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.vaticle.typedb.benchmark.typedb.agent

import com.vaticle.typedb.benchmark.common.concept.Country
import com.vaticle.typedb.benchmark.common.params.Context
import com.vaticle.typedb.benchmark.simulation.agent.CitizenshipAgent
import com.vaticle.typedb.benchmark.typedb.Labels.BIRTH_DATE
import com.vaticle.typedb.benchmark.typedb.Labels.CITIZEN
import com.vaticle.typedb.benchmark.typedb.Labels.CITIZENSHIP
import com.vaticle.typedb.benchmark.typedb.Labels.CODE
import com.vaticle.typedb.benchmark.typedb.Labels.COUNTRY
import com.vaticle.typedb.benchmark.typedb.Labels.PERSON
import com.vaticle.typedb.benchmark.typedb.driver.TypeDBClient
import com.vaticle.typedb.benchmark.typedb.driver.TypeDBTransaction
import com.vaticle.typeql.lang.TypeQL.match
import com.vaticle.typeql.lang.TypeQL.rel
import com.vaticle.typeql.lang.TypeQL.`var`
import java.time.LocalDateTime
import java.util.stream.Collectors.toList

class TypeDBCitizenshipAgent(client: TypeDBClient, context: Context) : CitizenshipAgent<TypeDBTransaction>(client, context) {
    override fun matchCitizenship(tx: TypeDBTransaction, country: Country, today: LocalDateTime) {
        tx.query().match(match(
            `var`(COUNTRY).isa(COUNTRY).has(CODE, country.code),
            `var`(CITIZEN).isa(PERSON).has(BIRTH_DATE, today),
            rel(CITIZEN, CITIZEN).rel(COUNTRY, COUNTRY).isa(CITIZENSHIP)
        )).collect(toList())
    }
}
