package org.dimensinfin.eveonline.neocom.visual.tests.mining.miningextractionstoday

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdMining200Ok

class MiningExtractionGenerator {
	val jsonMapper = ObjectMapper()
	fun searchExtraction(extractionIdentifier: String, hour: Int): String {
		return matchRequest(extractionIdentifier)
	}

	fun generateExtraction(extractionIdentifier: String, hour: Int): List<MiningExtraction> {
		jsonMapper.registerModule(JodaModule())
		var extractionSource = matchRequest(extractionIdentifier)
//		val mapper = ObjectMapper()
		var minings = jsonMapper.readValue<GetCharactersCharacterIdMining200Ok>(extractionSource, object : TypeReference<List<GetCharactersCharacterIdMining200Ok>>() {})
//		val gson = Gson()
//		val minings = gson?.fromJson(extractionSource, Array<GetCharactersCharacterIdMining200Ok>::class.java)
		val results = mutableListOf<MiningExtraction>()
		for (mine in minings) {
			results.add(MiningExtraction.Builder().withExtractionHour(hour).fromMining(mine).build())
		}
		return results
	}

	fun matchRequest(uri: String): String {
		return when {
			matches(uri, "93813310", "12", "30") -> extraction1230
			else -> {
				"[]"
			}
		}
	}

	fun matches(url: String, match1: String): Boolean {
		if (url.contains(match1)) return true
		return false
	}

	fun matches(url: String, match1: String, match2: String): Boolean {
		if (url.contains(match1) && url.contains(match2)) return true
		return false
	}

	fun matches(url: String, match1: String, match2: String, match3: String): Boolean {
		if (url.contains(match1) && url.contains(match2) && url.contains(match3)) return true
		return false
	}

	fun matches(url: String, match1: String, match2: String, match3: String, match4: String):
			Boolean {
		if (url.contains(match1) && url.contains(match2)
				&& url.contains(match3) && url.contains(match4)) return true
		return false
	}
}

private operator fun GetCharactersCharacterIdMining200Ok.iterator(): Iterator<GetCharactersCharacterIdMining200Ok> {
	TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}

// - M O C K   D A T A
const val extraction1230 = """
[
  {
    "date": "2019-08-06",
    "quantity": 1566,
    "solarSystemId": 30001735,
    "typeId": 17471
  },
  {
    "date": "2019-08-06",
    "quantity": 421,
    "solarSystemId": 30001735,
    "typeId": 17459
  }
]
"""

const val extraction1300 = """
[
  {
    "date": "2019-08-06",
    "quantity": 19276,
    "solarSystemId": 30001735,
    "type_id": 17471
  },
  {
    "date": "2019-08-06",
    "quantity": 5894,
    "solarSystemId": 30001735,
    "type_id": 17459
  }
]
"""

const val extraction1330 = """
[
  {
    "date": "2019-08-06",
    "quantity": 25432,
    "solarSystemId": 30001735,
    "type_id": 17471
  },
  {
    "date": "2019-08-06",
    "quantity": 23576,
    "solarSystemId": 30001735,
    "type_id": 17459
  },
  {
    "date": "2019-08-06",
    "quantity": 30348,
    "solarSystemId": 30001735,
    "type_id": 17464
  }
]
"""

const val extraction1400= """
[
  {
    "date": "2019-08-06",
    "quantity": 25432,
    "solar_system_id": 30001735,
    "type_id": 17471
  },
  {
    "date": "2019-08-06",
    "quantity": 24609,
    "solar_system_id": 30001735,
    "type_id": 17459
  },
  {
    "date": "2019-08-06",
    "quantity": 32667,
    "solar_system_id": 30001735,
    "type_id": 17464
  },
  {
    "date": "2019-08-08",
    "quantity": 14511,
    "solar_system_id": 30001735,
    "type_id": 17459
  },
  {
    "date": "2019-08-09",
    "quantity": 6285,
    "solar_system_id": 30001740,
    "type_id": 17453
  },
  {
    "date": "2019-08-09",
    "quantity": 11312,
    "solar_system_id": 30001740,
    "type_id": 17460
  },
  {
    "date": "2019-08-09",
    "quantity": 31207,
    "solar_system_id": 30001740,
    "type_id": 17464
  },
  {
    "date": "2019-08-09",
    "quantity": 20240,
    "solar_system_id": 30001740,
    "type_id": 17471
  }
]
"""