import Ontology.LoadOntology.LoadOntology;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.net.URISyntaxException;

public class MainOwl {
    public static void main(String[] args) {
        LoadOntology printer
                = new LoadOntology();
        try {
            printer.createManager();
            printer.setMapper();
            OWLDataFactory dataFactory = printer.getDataFactory();
            DefaultPrefixManager prefixManager = printer.defaultPrefixManager();
            OWLReasoner reasoner = printer.owlReasoner();
            reasoner.precomputeInferences(InferenceType.values());
            OWLClass transporter = dataFactory.getOWLClass(IRI.create(prefixManager.getDefaultPrefix(), "Average"));

            NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(transporter, false);

            for(Node<OWLNamedIndividual> individual : individualsNodeSet){
                individual.entities().forEach(x-> System.out.println(prefixManager.getShortForm(x)));
            }

            OWLIndividual newAverage = dataFactory.getOWLNamedIndividual(IRI.create(prefixManager.getDefaultPrefix() + "Average_10"));
            OWLClass average =dataFactory.getOWLClass(prefixManager.getDefaultPrefix()  + "Average");
            OWLClassAssertionAxiom axiom = dataFactory.getOWLClassAssertionAxiom(average,newAverage);
            AddAxiom addAxiom1 = new AddAxiom(printer.getOntology(), axiom);
            printer.applyChanges(addAxiom1);

            NodeSet<OWLNamedIndividual> individualsNodeSet2 = reasoner.getInstances(transporter, false);

            for(Node<OWLNamedIndividual> individual : individualsNodeSet2){
                individual.entities().forEach(x-> System.out.println(prefixManager.getShortForm(x)));
            }
            printer.saveOntology();
        } catch (OWLOntologyCreationException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
