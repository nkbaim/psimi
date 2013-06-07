package psidev.psi.mi.jami.binary.expansion;

import junit.framework.Assert;
import org.junit.Test;
import psidev.psi.mi.jami.binary.BinaryInteraction;
import psidev.psi.mi.jami.binary.BinaryInteractionEvidence;
import psidev.psi.mi.jami.binary.ModelledBinaryInteraction;
import psidev.psi.mi.jami.binary.impl.*;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.model.impl.*;
import psidev.psi.mi.jami.utils.CvTermUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * Unit tester for MatrixExpansion
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>07/06/13</pre>
 */

public class MatrixExpansionTest {

    private MatrixExpansion expansion = new MatrixExpansion();

    @Test
    public void test_method(){

        Assert.assertEquals(CvTermUtils.createMICvTerm(ComplexExpansionMethod.MATRIX_EXPANSION, ComplexExpansionMethod.MATRIX_EXPANSION_MI), expansion.getMethod());
    }

    @Test
    public void test_is_expandable(){

        Assert.assertFalse(expansion.isModelledInteractionExpandable(null));
        Assert.assertFalse(expansion.isModelledInteractionExpandable(new DefaultModelledInteraction()));
        Assert.assertFalse(expansion.isInteractionEvidenceExpandable(null));
        Assert.assertFalse(expansion.isInteractionEvidenceExpandable(new DefaultInteractionEvidence()));
        Assert.assertFalse(expansion.isInteractionExpandable(null));
        Assert.assertFalse(expansion.isInteractionExpandable(new DefaultInteraction()));

        InteractionEvidence interactionEvidence = new DefaultInteractionEvidence();
        interactionEvidence.addParticipantEvidence(new DefaultParticipantEvidence(new DefaultProtein("p1")));
        ModelledInteraction modelledInteraction = new DefaultModelledInteraction();
        modelledInteraction.addModelledParticipant(new DefaultModelledParticipant(new DefaultProtein("p1")));
        InteractionEvidence interaction = new DefaultInteractionEvidence();
        interaction.addParticipantEvidence(new DefaultParticipantEvidence(new DefaultProtein("p1")));
        Assert.assertTrue(expansion.isInteractionEvidenceExpandable(interactionEvidence));
        Assert.assertTrue(expansion.isModelledInteractionExpandable(modelledInteraction));
        Assert.assertTrue(expansion.isInteractionExpandable(interaction));
    }

    @Test
    public void test_expand_default_interaction(){

        InteractionEvidence binary = new DefaultInteractionEvidence();
        binary.addParticipantEvidence(new DefaultParticipantEvidence(new DefaultProtein("p1")));
        binary.addParticipantEvidence(new DefaultParticipantEvidence(new DefaultProtein("p2")));

        InteractionEvidence self_intra = new DefaultInteractionEvidence();
        self_intra.addParticipantEvidence(new DefaultParticipantEvidence(new DefaultProtein("p1"), new DefaultStoichiometry(1)));

        InteractionEvidence self_inter= new DefaultInteractionEvidence();
        self_inter.addParticipantEvidence(new DefaultParticipantEvidence(new DefaultProtein("p1"), new DefaultStoichiometry(3)));

        InteractionEvidence nary = new DefaultInteractionEvidence();
        ParticipantEvidence p1 = new DefaultParticipantEvidence(new DefaultProtein("p1"));
        ParticipantEvidence p2 = new DefaultParticipantEvidence(new DefaultProtein("p2"));
        ParticipantEvidence p3 = new DefaultParticipantEvidence(new DefaultProtein("p3"));
        nary.addParticipantEvidence(p1);
        nary.addParticipantEvidence(p2);
        nary.addParticipantEvidence(p3);

        Collection<BinaryInteraction> binaryExpanded = expansion.expandInteraction(binary);
        Assert.assertEquals(1, binaryExpanded.size());
        Assert.assertTrue(binaryExpanded.iterator().next() instanceof BinaryInteractionWrapper);
        Iterator<? extends Participant> iterator = binary.getParticipants().iterator();
        Assert.assertTrue(binaryExpanded.iterator().next().getParticipantA() == iterator.next());
        Assert.assertTrue(binaryExpanded.iterator().next().getParticipantB() == iterator.next());

        Collection<BinaryInteraction> self_intra_expanded = expansion.expandInteraction(self_intra);
        Assert.assertEquals(1, self_intra_expanded.size());
        Assert.assertTrue(self_intra_expanded.iterator().next() instanceof BinaryInteractionWrapper);
        Iterator<? extends Participant> iterator2 = self_intra.getParticipants().iterator();
        Assert.assertTrue(self_intra_expanded.iterator().next().getParticipantA() == iterator2.next());
        Assert.assertNull(self_intra_expanded.iterator().next().getParticipantB());

        Collection<BinaryInteraction> self_inter_expanded = expansion.expandInteraction(self_inter);
        Assert.assertEquals(1, self_inter_expanded.size());
        Assert.assertTrue(self_inter_expanded.iterator().next() instanceof DefaultBinaryInteraction);
        Iterator<? extends Participant> iterator3 = self_inter.getParticipants().iterator();
        Assert.assertTrue(self_inter_expanded.iterator().next().getParticipantA() == iterator3.next());
        Assert.assertNotNull(self_inter_expanded.iterator().next().getParticipantB());
        Assert.assertEquals(new DefaultStoichiometry(0), self_inter_expanded.iterator().next().getParticipantB().getStoichiometry());

        Collection<BinaryInteraction> nary_expanded = expansion.expandInteraction(nary);
        Assert.assertEquals(3, nary_expanded.size());
        for (BinaryInteraction binaryInteraction : nary_expanded){
            Assert.assertTrue(binaryInteraction instanceof DefaultBinaryInteraction);
            Assert.assertTrue(binaryInteraction.getParticipantB() == p2 || binaryInteraction.getParticipantB() == p1 || binaryInteraction.getParticipantB() == p3);
            Assert.assertTrue(binaryInteraction.getParticipantA() == p2 || binaryInteraction.getParticipantA() == p1 || binaryInteraction.getParticipantA() == p3);
            Assert.assertTrue(binaryInteraction.getParticipantA() != binaryInteraction.getParticipantB());
        }
    }

    @Test
    public void test_expand_interaction_evidence(){

        InteractionEvidence binary = new DefaultInteractionEvidence();
        binary.addParticipantEvidence(new DefaultParticipantEvidence(new DefaultProtein("p1")));
        binary.addParticipantEvidence(new DefaultParticipantEvidence(new DefaultProtein("p2")));

        InteractionEvidence self_intra = new DefaultInteractionEvidence();
        self_intra.addParticipantEvidence(new DefaultParticipantEvidence(new DefaultProtein("p1"), new DefaultStoichiometry(1)));

        InteractionEvidence self_inter= new DefaultInteractionEvidence();
        self_inter.addParticipantEvidence(new DefaultParticipantEvidence(new DefaultProtein("p1"), new DefaultStoichiometry(3)));

        InteractionEvidence nary = new DefaultInteractionEvidence();
        ParticipantEvidence p1 = new DefaultParticipantEvidence(new DefaultProtein("p1"));
        ParticipantEvidence p2 = new DefaultParticipantEvidence(new DefaultProtein("p2"));
        ParticipantEvidence p3 = new DefaultParticipantEvidence(new DefaultProtein("p3"));
        nary.addParticipantEvidence(p1);
        nary.addParticipantEvidence(p2);
        nary.addParticipantEvidence(p3);

        Collection<BinaryInteractionEvidence> binaryExpanded = expansion.expandInteractionEvidence(binary);
        Assert.assertEquals(1, binaryExpanded.size());
        Assert.assertTrue(binaryExpanded.iterator().next() instanceof BinaryInteractionEvidenceWrapper);
        Iterator<? extends Participant> iterator = binary.getParticipants().iterator();
        Assert.assertTrue(binaryExpanded.iterator().next().getParticipantA() == iterator.next());
        Assert.assertTrue(binaryExpanded.iterator().next().getParticipantB() == iterator.next());

        Collection<BinaryInteractionEvidence> self_intra_expanded = expansion.expandInteractionEvidence(self_intra);
        Assert.assertEquals(1, self_intra_expanded.size());
        Assert.assertTrue(self_intra_expanded.iterator().next() instanceof BinaryInteractionEvidenceWrapper);
        Iterator<? extends Participant> iterator2 = self_intra.getParticipants().iterator();
        Assert.assertTrue(self_intra_expanded.iterator().next().getParticipantA() == iterator2.next());
        Assert.assertNull(self_intra_expanded.iterator().next().getParticipantB());

        Collection<BinaryInteractionEvidence> self_inter_expanded = expansion.expandInteractionEvidence(self_inter);
        Assert.assertEquals(1, self_inter_expanded.size());
        Assert.assertTrue(self_inter_expanded.iterator().next() instanceof DefaultBinaryInteractionEvidence);
        Iterator<? extends Participant> iterator3 = self_inter.getParticipants().iterator();
        Assert.assertTrue(self_inter_expanded.iterator().next().getParticipantA() == iterator3.next());
        Assert.assertNotNull(self_inter_expanded.iterator().next().getParticipantB());
        Assert.assertEquals(new DefaultStoichiometry(0), self_inter_expanded.iterator().next().getParticipantB().getStoichiometry());

        Collection<BinaryInteractionEvidence> nary_expanded = expansion.expandInteractionEvidence(nary);
        Assert.assertEquals(3, nary_expanded.size());
        for (BinaryInteraction binaryInteraction : nary_expanded){
            Assert.assertTrue(binaryInteraction instanceof DefaultBinaryInteractionEvidence);
            Assert.assertTrue(binaryInteraction.getParticipantB() == p2 || binaryInteraction.getParticipantB() == p1 || binaryInteraction.getParticipantB() == p3);
            Assert.assertTrue(binaryInteraction.getParticipantA() == p2 || binaryInteraction.getParticipantA() == p1 || binaryInteraction.getParticipantA() == p3);
            Assert.assertTrue(binaryInteraction.getParticipantA() != binaryInteraction.getParticipantB());
        }
    }

    @Test
    public void test_expand_modelled_interaction(){

        ModelledInteraction binary = new DefaultModelledInteraction();
        binary.addModelledParticipant(new DefaultModelledParticipant(new DefaultProtein("p1")));
        binary.addModelledParticipant(new DefaultModelledParticipant(new DefaultProtein("p2")));

        ModelledInteraction self_intra = new DefaultModelledInteraction();
        self_intra.addModelledParticipant(new DefaultModelledParticipant(new DefaultProtein("p1"), new DefaultStoichiometry(1)));

        ModelledInteraction self_inter= new DefaultModelledInteraction();
        self_inter.addModelledParticipant(new DefaultModelledParticipant(new DefaultProtein("p1"), new DefaultStoichiometry(3)));

        ModelledInteraction nary = new DefaultModelledInteraction();
        ModelledParticipant p1 = new DefaultModelledParticipant(new DefaultProtein("p1"));
        ModelledParticipant p2 = new DefaultModelledParticipant(new DefaultProtein("p2"));
        ModelledParticipant p3 = new DefaultModelledParticipant(new DefaultProtein("p3"));
        nary.addModelledParticipant(p1);
        nary.addModelledParticipant(p2);
        nary.addModelledParticipant(p3);

        Collection<ModelledBinaryInteraction> binaryExpanded = expansion.expandModelledInteraction(binary);
        Assert.assertEquals(1, binaryExpanded.size());
        Assert.assertTrue(binaryExpanded.iterator().next() instanceof ModelledBinaryInteractionWrapper);
        Iterator<? extends Participant> iterator = binary.getParticipants().iterator();
        Assert.assertTrue(binaryExpanded.iterator().next().getParticipantA() == iterator.next());
        Assert.assertTrue(binaryExpanded.iterator().next().getParticipantB() == iterator.next());

        Collection<ModelledBinaryInteraction> self_intra_expanded = expansion.expandModelledInteraction(self_intra);
        Assert.assertEquals(1, self_intra_expanded.size());
        Assert.assertTrue(self_intra_expanded.iterator().next() instanceof ModelledBinaryInteractionWrapper);
        Iterator<? extends Participant> iterator2 = self_intra.getParticipants().iterator();
        Assert.assertTrue(self_intra_expanded.iterator().next().getParticipantA() == iterator2.next());
        Assert.assertNull(self_intra_expanded.iterator().next().getParticipantB());

        Collection<ModelledBinaryInteraction> self_inter_expanded = expansion.expandModelledInteraction(self_inter);
        Assert.assertEquals(1, self_inter_expanded.size());
        Assert.assertTrue(self_inter_expanded.iterator().next() instanceof DefaultModelledBinaryInteraction);
        Iterator<? extends Participant> iterator3 = self_inter.getParticipants().iterator();
        Assert.assertTrue(self_inter_expanded.iterator().next().getParticipantA() == iterator3.next());
        Assert.assertNotNull(self_inter_expanded.iterator().next().getParticipantB());
        Assert.assertEquals(new DefaultStoichiometry(0), self_inter_expanded.iterator().next().getParticipantB().getStoichiometry());

        Collection<ModelledBinaryInteraction> nary_expanded = expansion.expandModelledInteraction(nary);
        Assert.assertEquals(3, nary_expanded.size());
        for (BinaryInteraction binaryInteraction : nary_expanded){
            Assert.assertTrue(binaryInteraction instanceof DefaultModelledBinaryInteraction);
            Assert.assertTrue(binaryInteraction.getParticipantB() == p2 || binaryInteraction.getParticipantB() == p1 || binaryInteraction.getParticipantB() == p3);
            Assert.assertTrue(binaryInteraction.getParticipantA() == p2 || binaryInteraction.getParticipantA() == p1 || binaryInteraction.getParticipantA() == p3);
            Assert.assertTrue(binaryInteraction.getParticipantA() != binaryInteraction.getParticipantB());
        }
    }
}
