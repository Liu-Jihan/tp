package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.LastVisit;
import seedu.address.model.person.Patient;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for RemarkCommand.
 */
public class LastVisitCommandTest {

    private static final String REMARK_STUB = "Some remark";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addRemarkUnfilteredList_success() {
        Patient firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Patient editedPerson = new PersonBuilder(firstPerson).withLastVisit(REMARK_STUB).build();

        LastVisitCommand lastVisitCommand = new LastVisitCommand(INDEX_FIRST_PERSON,
                new LastVisit(editedPerson.getLastVisit().value));

        String expectedMessage = String.format(LastVisitCommand.MESSAGE_ADD_LAST_VISIT_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(lastVisitCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteRemarkUnfilteredList_success() {
        Patient firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Patient editedPerson = new PersonBuilder(firstPerson).withLastVisit("").build();

        LastVisitCommand lastVisitCommand = new LastVisitCommand(INDEX_FIRST_PERSON,
                new LastVisit(editedPerson.getLastVisit().toString()));

        String expectedMessage = String.format(LastVisitCommand.MESSAGE_DELETE_LAST_VISIT_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(lastVisitCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Patient firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Patient editedPerson = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withLastVisit(REMARK_STUB).build();

        LastVisitCommand lastVisitCommand = new LastVisitCommand(INDEX_FIRST_PERSON,
                new LastVisit(editedPerson.getLastVisit().value));

        String expectedMessage = String.format(LastVisitCommand.MESSAGE_ADD_LAST_VISIT_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(lastVisitCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LastVisitCommand lastVisitCommand = new LastVisitCommand(outOfBoundIndex, new LastVisit(VALID_REMARK_BOB));

        assertCommandFailure(lastVisitCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        LastVisitCommand lastVisitCommand = new LastVisitCommand(outOfBoundIndex, new LastVisit(VALID_REMARK_BOB));

        assertCommandFailure(lastVisitCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final LastVisitCommand standardCommand = new LastVisitCommand(INDEX_FIRST_PERSON,
                new LastVisit(VALID_REMARK_AMY));

        // same values -> returns true
        LastVisitCommand commandWithSameValues = new LastVisitCommand(INDEX_FIRST_PERSON,
                new LastVisit(VALID_REMARK_AMY));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new LastVisitCommand(INDEX_SECOND_PERSON,
                new LastVisit(VALID_REMARK_AMY))));

        // different remark -> returns false
        assertFalse(standardCommand.equals(new LastVisitCommand(INDEX_FIRST_PERSON,
                new LastVisit(VALID_REMARK_BOB))));
    }
}
