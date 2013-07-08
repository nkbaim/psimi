package psidev.psi.mi.jami.tab.io.writer;

import psidev.psi.mi.jami.binary.BinaryInteraction;
import psidev.psi.mi.jami.binary.expansion.ComplexExpansionMethod;
import psidev.psi.mi.jami.binary.expansion.SpokeExpansion;
import psidev.psi.mi.jami.exception.MIIOException;
import psidev.psi.mi.jami.factory.InteractionWriterFactory;
import psidev.psi.mi.jami.model.Interaction;
import psidev.psi.mi.jami.model.InteractionEvidence;
import psidev.psi.mi.jami.model.ModelledInteraction;
import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.tab.MitabVersion;

import java.io.*;

/**
 * The simple MITAB 2.5 writer will write interactions using the JAMI interfaces.
 *
 * It will not check for MITAB extended objects (such as MitabAlias and MitabFeature).
 *
 * The default Complex expansion method is spoke expansion.
 *
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>10/06/13</pre>
 */

public class Mitab25Writer extends AbstractMitabWriter<Interaction, BinaryInteraction, Participant> {

    private Mitab25ModelledInteractionWriter modelledInteractionWriter;
    private Mitab25InteractionEvidenceWriter interactionEvidenceWriter;
    private Writer writer;

    public Mitab25Writer() {
        super();
    }

    public Mitab25Writer(File file) throws IOException {
        super(file);
    }

    public Mitab25Writer(OutputStream output) {
        super(output);
    }

    public Mitab25Writer(Writer writer) {
        super(writer);
    }

    public Mitab25Writer(File file, ComplexExpansionMethod<Interaction, BinaryInteraction> expansionMethod) throws IOException {
        super(file, expansionMethod);
    }

    public Mitab25Writer(OutputStream output, ComplexExpansionMethod<Interaction, BinaryInteraction> expansionMethod) throws IOException {
        super(output, expansionMethod);
    }

    public Mitab25Writer(Writer writer, ComplexExpansionMethod<Interaction, BinaryInteraction> expansionMethod) throws IOException {
        super(writer, expansionMethod);
    }

    @Override
    public void start() throws MIIOException {
        super.start();
        this.modelledInteractionWriter.start();
        this.interactionEvidenceWriter.start();
    }

    @Override
    public void write(Interaction interaction) throws MIIOException {
        if (this.interactionEvidenceWriter == null || this.modelledInteractionWriter == null){
            throw new IllegalStateException("The Mitab writer has not been initialised. The options for the Mitab writer should contain at least "+ InteractionWriterFactory.OUTPUT_OPTION_KEY + " to know where to write the interactions.");
        }

        if (interaction instanceof InteractionEvidence){
            this.interactionEvidenceWriter.write((InteractionEvidence) interaction);
            if (!hasStarted()){
                this.modelledInteractionWriter.start();
                setStarted(true);
            }
        }
        else if (interaction instanceof ModelledInteraction){
            this.modelledInteractionWriter.write((ModelledInteraction) interaction);
            if (!hasStarted()){
                this.interactionEvidenceWriter.start();
                setStarted(true);
            }
        }
        else {
            if (!hasStarted()){
                this.modelledInteractionWriter.start();
                this.interactionEvidenceWriter.start();
            }
            super.write(interaction);
        }
    }

    @Override
    public void close() throws MIIOException {
        try{
            super.close();
        }
        finally {
            this.modelledInteractionWriter = null;
            this.interactionEvidenceWriter = null;
            this.writer = null;
        }
    }

    @Override
    public void reset() throws MIIOException {
        try{
            super.reset();
        }
        finally {
            this.modelledInteractionWriter = null;
            this.interactionEvidenceWriter = null;
            this.writer = null;
        }
    }

    @Override
    public MitabVersion getVersion() {
        return MitabVersion.v2_5;
    }

    @Override
    protected void initialiseExpansionMethod(ComplexExpansionMethod<Interaction, BinaryInteraction> expansionMethod) {
        setExpansionMethod(expansionMethod != null ? expansionMethod : new SpokeExpansion());
    }

    @Override
    protected void initialiseWriter(Writer writer) {
        this.writer = writer;
        setBinaryWriter(new Mitab25BinaryWriter(this.writer));
        initialiseSubWriters();
    }

    @Override
    protected void initialiseOutputStream(OutputStream output) {
        this.writer = new OutputStreamWriter(output);
        setBinaryWriter(new Mitab25BinaryWriter(writer));
        initialiseSubWriters();
    }

    @Override
    protected void initialiseFile(File file) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(file));
        initialiseSubWriters();
    }

    protected void initialiseSubWriters() {
        setBinaryWriter(new Mitab25BinaryWriter(writer));
        this.modelledInteractionWriter = new Mitab25ModelledInteractionWriter(writer);
        this.modelledInteractionWriter.setWriteHeader(false);
        this.interactionEvidenceWriter = new Mitab25InteractionEvidenceWriter(writer);
        this.interactionEvidenceWriter.setWriteHeader(false);
    }

    protected void setModelledInteractionWriter(Mitab25ModelledInteractionWriter modelledInteractionWriter) {
        this.modelledInteractionWriter = modelledInteractionWriter;
        this.modelledInteractionWriter.setWriteHeader(false);
    }

    protected void setInteractionEvidenceWriter(Mitab25InteractionEvidenceWriter interactionEvidenceWriter) {
        this.interactionEvidenceWriter = interactionEvidenceWriter;
        this.interactionEvidenceWriter.setWriteHeader(false);
    }

    protected Writer getWriter() {
        return writer;
    }
}
