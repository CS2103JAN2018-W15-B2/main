package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.LinkedList;

import seedu.address.model.patient.Patient;
import seedu.address.model.patient.exceptions.DuplicatePatientException;

/**
 * Patient visiting queue in Imdb
 * Gurantees: immutable
 */
public class UniquePatientVisitingQueue implements Iterable<Patient> {

    private LinkedList<Patient> visitingQueue;

    public UniquePatientVisitingQueue() {
        visitingQueue = new LinkedList<Patient>();
    }

    /**
     * Adds a patient to the visiting queue.
     *
     * @throws DuplicatePatientException if the patient to add is a duplicate of an existing patient in the queue.
     */
    public void add(Patient patient) throws DuplicatePatientException {
        requireNonNull(patient);

        if (contains(patient)) {
            throw new DuplicatePatientException();
        }

        visitingQueue.add(patient);
    }

    public Patient getNextPatient() {
        return visitingQueue.peekFirst();
    }

    public Patient removePatient() {
        return visitingQueue.removeFirst();
    }

    public LinkedList<Patient> getVisitingQueue() {
        return visitingQueue;
    }

    /**
     * Returns true if the queue contains an equivalent patient as the given argument.
     */
    public boolean contains(Patient toCheck) {
        requireNonNull(toCheck);
        return visitingQueue.contains(toCheck);
    }

    @Override
    public Iterator<Patient> iterator() {
        return visitingQueue.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePatientVisitingQueue // instanceof handles nulls
                && this.visitingQueue.equals(((UniquePatientVisitingQueue) other).visitingQueue));
    }

    @Override
    public int hashCode() {
        return visitingQueue.hashCode();
    }
}
