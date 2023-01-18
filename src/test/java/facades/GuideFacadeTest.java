package facades;

import TestEnvironment.TestEnvironment;
import dtos.GuideDTO;
import dtos.PersonDTO;
import entities.Guide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GuideFacadeTest extends TestEnvironment {

    private static GuideFacade facade;

    public GuideFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        TestEnvironment.setUpClass();
        facade = GuideFacade.getFacade(emf);
    }

    @Test
    public void getGuideByIdTest() {
        GuideDTO expected = new GuideDTO(createAndPersistGuide());

        GuideDTO actual = new GuideDTO(facade.getGuideById(expected.getId()));

        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void getAllGuidesTest() {
        GuideDTO guideDTO = new GuideDTO(createAndPersistGuide());
        createAndPersistGuide();
        createAndPersistGuide();

        List<GuideDTO> allGuides = facade.getAllGuides();
        assertEquals(3, allGuides.size());
        assertTrue(allGuides.contains(guideDTO));
    }

    @Test
    public void createGuideTest() {
        Guide guide = createGuide();

        Guide createdGuide = facade.createGuide(guide);

        assertDatabaseHasEntity(createdGuide, createdGuide.getId());


    }

}
