package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ILLNESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYMPTOM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TREATMENT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.patient.Patient;
import seedu.address.model.patient.Record;
import seedu.address.model.patient.RecordList;
import seedu.address.model.patient.exceptions.DuplicatePatientException;
import seedu.address.model.patient.exceptions.PatientNotFoundException;

/**
 * Adds a patient to the address book.
 */
public class RecordCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "record";
    public static final String COMMAND_ALIAS = "rec";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the medical record of a patient in the address book. "
            + "Parameters: "
            + PREFIX_DATE + "DATE "
            + PREFIX_SYMPTOM + "SYMPTOM "
            + PREFIX_ILLNESS + "ILLNESS "
            + PREFIX_TREATMENT + "TREATMENT "
            + "\n"
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + PREFIX_DATE + "1st March 2018 "
            + PREFIX_SYMPTOM + "Headache, runny nose "
            + PREFIX_ILLNESS + "Flu "
            + PREFIX_TREATMENT + "Zyrtec";

    public static final String MESSAGE_EDIT_RECORD_SUCCESS = "Medical record updated: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This patient already exists in the address book.";

    private final Index patientIndex;
    private final int recordIndex;
    private final Record record;

    private Patient patientToEdit;
    private Patient editedPatient;

    /**
     * Creates a RecordCommand to edit the records of the specified {@code Patient}
     */
    public RecordCommand(Index patientIndex, int recordIndex, Record record) {
        requireNonNull(patientIndex);
        requireNonNull(record);
        this.patientIndex = patientIndex;
        this.recordIndex = recordIndex;
        this.record = record;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updatePerson(patientToEdit, editedPatient);
        } catch (DuplicatePatientException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PatientNotFoundException pnfe) {
            throw new AssertionError("The target patient cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_RECORD_SUCCESS, editedPatient));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Patient> lastShownList = model.getFilteredPersonList();

        if (patientIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        patientToEdit = lastShownList.get(patientIndex.getZeroBased());
        editedPatient = createEditedPerson(patientToEdit, recordIndex, record);
    }

    /**
     * Creates and returns a {@code Patient} with the details of {@code patientToEdit}
     * edited with {@code record}.
     */
    private static Patient createEditedPerson(Patient patientToEdit, int recordIndex, Record record) {
        assert patientToEdit != null;

        RecordList recordlist = patientToEdit.getRecordList();
        recordlist.edit(recordIndex, record);

        return new Patient(patientToEdit.getName(), patientToEdit.getNric(), patientToEdit.getPhone(),
                patientToEdit.getEmail(), patientToEdit.getAddress(), patientToEdit.getDob(),
                patientToEdit.getBloodType(), patientToEdit.getRemark(), recordlist, patientToEdit.getTags());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RecordCommand)) {
            return false;
        }

        // state check
        RecordCommand e = (RecordCommand) other;

        return getPatientIndex().equals(e.getPatientIndex())
                && getRecord().equals(e.getRecord());
    }

    public Index getPatientIndex() {
        return patientIndex;
    }
    public int getRecordIndex() {
        return recordIndex;
    }
    public Record getRecord() {
        return record;
    }
    public Patient getToEdit() {
        return patientToEdit;
    }
    public Patient getEdited() {
        return editedPatient;
    }
}