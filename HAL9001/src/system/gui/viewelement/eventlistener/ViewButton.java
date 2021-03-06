package system.gui.viewelement.eventlistener;

import system.gui.event.*;
import system.gui.event.criteria.CriteriaPacket;
import system.gui.event.criteria.GamepadEventCriteria;
import util.control.Button;

/**
 * A menu button that can be made to run tasks on certain click events.
 * <p>
 * Creation Date: 5/17/20
 *
 * @author Cole Savage, Level Up
 * @version 1.0.0
 * @see ClickEvent
 * @see OnClickEvent
 * @see WhileClickEvent
 * @see OnClickReleaseEvent
 * @see AdvancedListener
 * @see EntireViewButton
 * @see HandlesEvents
 * @since 1.1.0
 */
@HandlesEvents(events = {OnClickEvent.class, WhileClickEvent.class, OnClickReleaseEvent.class})
public class ViewButton implements AdvancedListener {
    //The text that will be associated with the button.
    private String text;
    //Task packets for running various types of tasks associated with the button.
    private TaskPacket<Button<Boolean>>
            onClickTasks = new TaskPacket<>(),
            whileClickedTasks = new TaskPacket<>(),
            onClickReleasedTasks = new TaskPacket<>(),
            backgroundTasks = new TaskPacket<>();

    /**
     * Constructor for ViewButton.
     *
     * @param text The text that will be associated with the button.
     */
    public ViewButton(String text) {
        this.text = text;
    }

    /**
     * Adds an onClick task to the onClick task packet.
     *
     * @param button The button being clicked.
     * @param task The task to run.
     * @return This button.
     *
     * @see Button
     * @see Task
     * @see TaskPacket
     */
    public ViewButton onClick(Button<Boolean> button, Task task) {
        onClickTasks.add(button, task);
        return this;
    }

    /**
     * Adds an whileClicked task to the whileClicked task packet.
     *
     * @param button The button being held.
     * @param task The task to run.
     * @return This button.
     *
     * @see Button
     * @see Task
     * @see TaskPacket
     */
    public ViewButton whileClicked(Button<Boolean> button, Task task) {
        whileClickedTasks.add(button, task);
        return this;
    }

    /**
     * Adds an onClickReleased task to the onClickReleased task packet.
     *
     * @param button The button being released.
     * @param task The task to run.
     * @return This button.
     *
     * @see Button
     * @see Task
     * @see TaskPacket
     */
    public ViewButton onClickReleased(Button<Boolean> button, Task task) {
        onClickReleasedTasks.add(button, task);
        return this;
    }

    /**
     * Adds a background task to the background task packet.
     *
     * @param task The task to run.
     * @return This button.
     */
    public ViewButton addBackgroundTask(Task task) {
        backgroundTasks.add(task);
        return this;
    }

    @Override
    public CriteriaPacket getCriteria() {
        //Create event criteria.
        GamepadEventCriteria<OnClickEvent, Button<Boolean>> buttonCriteria = new GamepadEventCriteria<>(onClickTasks.getValidKeys());
        GamepadEventCriteria<WhileClickEvent, Button<Boolean>> whileClickButtonCriteria = new GamepadEventCriteria<>(whileClickedTasks.getValidKeys());
        GamepadEventCriteria<OnClickReleaseEvent, Button<Boolean>> onClickReleaseEventGamepadEventCriteria = new GamepadEventCriteria<>(onClickReleasedTasks.getValidKeys());

        //Create criteria packet.
        CriteriaPacket criteriaPacket = new CriteriaPacket();
        criteriaPacket.add(buttonCriteria);
        criteriaPacket.add(whileClickButtonCriteria);
        criteriaPacket.add(onClickReleaseEventGamepadEventCriteria);

        return criteriaPacket;
    }

    @Override
    public boolean onEvent(Event eventIn) {
        if(eventIn instanceof LoopEvent) {
            backgroundTasks.runTasks(new DataPacket(eventIn, this));
        } else if(eventIn instanceof OnClickEvent) {
            OnClickEvent event = (OnClickEvent) eventIn;
            onClickTasks.runTasks(event.getButton(), new DataPacket(event, this));
            return true;
        } else if(eventIn instanceof WhileClickEvent) {
            WhileClickEvent event = (WhileClickEvent) eventIn;
            whileClickedTasks.runTasks(event.getButton(), new DataPacket(event, this));
            return true;
        } else if(eventIn instanceof OnClickReleaseEvent) {
            OnClickReleaseEvent event = (OnClickReleaseEvent) eventIn;
            onClickReleasedTasks.runTasks(event.getButton(), new DataPacket(event, this));
            return true;
        }
        return false;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
