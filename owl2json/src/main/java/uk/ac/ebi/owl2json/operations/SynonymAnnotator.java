package uk.ac.ebi.owl2json.operations;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.ac.ebi.owl2json.OwlNode;
import uk.ac.ebi.owl2json.OwlTranslator;

public class SynonymAnnotator {

	public static void annotateSynonyms(OwlTranslator translator) {

		long startTime3 = System.nanoTime();


		Set<String> synonymProperties = new HashSet<String>();
		synonymProperties.add("http://www.geneontology.org/formats/oboInOwl#hasExactSynonym");


		Object configSynonymProperties = translator.config.get("synonym_property");

		if(configSynonymProperties instanceof Collection<?>) {
			synonymProperties.addAll((Collection<String>) configSynonymProperties);
		}


		for(String id : translator.nodes.keySet()) {
		    OwlNode c = translator.nodes.get(id);
		    if (c.type == OwlNode.NodeType.CLASS ||
				c.type == OwlNode.NodeType.PROPERTY ||
				c.type == OwlNode.NodeType.NAMED_INDIVIDUAL) {

			// skip bnodes
			if(c.uri == null)
				continue;

			for(String prop : synonymProperties) {
				List<OwlNode.Property> values = c.properties.properties.get(prop);
				if(values != null) {
					for(OwlNode.Property value : values) {
						c.properties.addProperty("synonym", value.value);
					}
				}
			}
		    }
		}
		long endTime3 = System.nanoTime();
		System.out.println("annotate synonyms: " + ((endTime3 - startTime3) / 1000 / 1000 / 1000));


	}
}
