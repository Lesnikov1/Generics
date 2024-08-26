import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import kotlin.test.assertEquals

class NoteServiceTest {

    @Before
    fun clearBeforeTest() {
        NoteService.clear()
    }

    @Test
    fun createCommentFindNote() {
        NoteService.add(Note(0, "title", "text"))
        assertEquals(1, NoteService.createComment(Comment("message", 1, 0)))
    }

    @Test(expected = NoteService.NoteNotFoundException::class)
    fun createCommentNotFindNote() {
        NoteService.add(Note(0, "title", "text"))
        NoteService.createComment(Comment("message", 2, 0))
    }

    @Test
    fun deleteFindNote() {
        NoteService.add(Note(0, "title", "text"))
        assertEquals(true, NoteService.delete(Note(1, "title", "text")))
    }

    @Test(expected = NoteService.NoteNotFoundException::class)
    fun deleteNotFindNote() {
        NoteService.delete(Note(1, "title", "text"))
    }

    @Test
    fun deleteCommentFindNoteFindComment(){
        NoteService.add(Note(0, "title", "text"))
        NoteService.createComment(Comment("message", 1, 0))
        assertEquals(true, NoteService.deleteComment(Comment("message", 1, 1)))
    }

    @Test(expected = NoteService.NoteNotFoundException::class)
    fun deleteCommentNotFindNote(){
        NoteService.add(Note(0, "title", "text"))
        NoteService.createComment(Comment("message", 1, 0))
        NoteService.deleteComment(Comment("message", 2, 1))
    }

    @Test(expected = NoteService.CommentNotFoundException::class)
    fun deleteCommentNotFindComment(){
        NoteService.add(Note(0, "title", "text"))
        NoteService.createComment(Comment("message", 1, 0))
        NoteService.deleteComment(Comment("message", 1, 2))
    }
}