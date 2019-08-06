package org.dimensinfin.eveonline.neocom.visual.tests.mining.miningextractionstoday

import com.google.gson.Gson
import org.dimensinfin.eveonline.neocom.database.entities.MiningExtraction
import org.dimensinfin.eveonline.neocom.esiswagger.model.GetCharactersCharacterIdMining200Ok

class MiningExtractionGenerator {
	fun generateExtraction(extractionIdentifier: String, hour: Int): List<MiningExtraction> {
		var extractionSource = matchRequest(extractionIdentifier)
//		val gson = Gson()
		val minings = Gson().fromJson(extractionSource, Array<GetCharactersCharacterIdMining200Ok>::class.java)
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

// - M O C K   D A T A
const val extraction1230 = """
[
  {
    "date": "2019-08-06",
    "quantity": 1566,
    "solar_system_id": 30001735,
    "type_id": 17471
  },
  {
    "date": "2019-08-06",
    "quantity": 421,
    "solar_system_id": 30001735,
    "type_id": 17459
  }
]
"""

const val extraction1300 = """
[
  {
    "date": "2019-08-06",
    "quantity": 19276,
    "solar_system_id": 30001735,
    "type_id": 17471
  },
  {
    "date": "2019-08-06",
    "quantity": 5894,
    "solar_system_id": 30001735,
    "type_id": 17459
  }
]
"""

const val extraction1330 = """
[
  {
    "date": "2019-08-06",
    "quantity": 25432,
    "solar_system_id": 30001735,
    "type_id": 17471
  },
  {
    "date": "2019-08-06",
    "quantity": 23576,
    "solar_system_id": 30001735,
    "type_id": 17459
  },
  {
    "date": "2019-08-06",
    "quantity": 30348,
    "solar_system_id": 30001735,
    "type_id": 17464
  }
]
"""