package Ontology.LoadOntology;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import javax.annotation.Nonnull;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

public class LoadOntology {
        OWLOntologyManager manager;
        OWLOntology ontology;
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        public void createManager() throws OWLOntologyCreationException, URISyntaxException {
            manager = OWLManager.createOWLOntologyManager();
            ontology = load(manager);
        }
        private OWLOntology load(@Nonnull OWLOntologyManager manager) throws OWLOntologyCreationException, URISyntaxException {
            return manager.loadOntologyFromOntologyDocument(
                    new File(Objects.requireNonNull(getClass().getClassLoader().getResource("OntologyTransporter.owl")).toURI()));
        }
        public OWLDataFactory getDataFactory(){
            return manager.getOWLDataFactory();
        }
        public void setMapper(){
            IRI ontologyIRI = IRI.create("http://www.semanticweb.org/faspe/ontologies/2020/11/projectwebsemanticotransportline");
            IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
            SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
            manager.getIRIMappers().add(mapper);
        }
        public DefaultPrefixManager defaultPrefixManager(){
            return new DefaultPrefixManager(null, null,
                    ontology.getOntologyID().getDefaultDocumentIRI()
                            .orElse(IRI.create("http://www.semanticweb.org/faspe/ontologies/2020/11/projectwebsemanticotransportline"))
                            .getIRIString().concat("#"));
        }
        public OWLOntology getOntology() {
            return ontology;
        }
        public OWLReasoner owlReasoner(){
            return reasonerFactory.createReasoner(ontology);
        }
        public void applyChanges(AddAxiom axiom){
            manager.applyChange(axiom);
        }
        public void saveOntology(){
            try {
                manager.saveOntology(ontology);
            } catch (OWLOntologyStorageException e) {
                e.printStackTrace();
            }
        }
}
