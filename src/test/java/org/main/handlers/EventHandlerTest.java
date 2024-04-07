package org.main.handlers;

import org.junit.jupiter.api.Test;
import org.main.dao.EventDAO;
import org.main.ressources.Event;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventHandlerTest {
    @Test
    public void testFilterEventByStatusAndCategory() {
        EventDAO eventDAO = mock(EventDAO.class);

        List<Event> events = new ArrayList<>();
        events.add(new Event("Event 1", "Category A", Event.Status.ACTIVE));
        events.add(new Event("Event 2", "Category B", Event.Status.ACTIVE));
        events.add(new Event("Event 3", "Category A", Event.Status.ACTIVE));
        events.add(new Event("Event 4", "Category C", Event.Status.ACTIVE));
        events.add(new Event("Event 5", "Category A", Event.Status.ACTIVE));

        when(eventDAO.getAll()).thenReturn(events);

        EventHandler handler = new EventHandler(eventDAO);

        List<Event> expectedEvents = new ArrayList<>();
        expectedEvents.add(new Event("Event 1", "Category A", Event.Status.ACTIVE));
        expectedEvents.add(new Event("Event 3", "Category A", Event.Status.ACTIVE));
        expectedEvents.add(new Event("Event 5", "Category A", Event.Status.ACTIVE));

        List<Event> filteredEvents = handler.filterEventByStatusAndCategory("Category A", Event.Status.ACTIVE);
        assertEquals(3, filteredEvents.size());

    }
}
