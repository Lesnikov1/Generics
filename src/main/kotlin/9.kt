data class Comment(
    val message: String,
    val idNote: Int,
    val idComment: Int
)

data class Note(
    val idNote: Int,
    var title: String,
    var text: String,
    val comments: MutableList<Comment> = mutableListOf()
)

object NoteService {
    val notes: MutableList<Note> = mutableListOf<Note>()
    private var lastIdNote = 0
    private var lastIdComment = 0
    private val deletedNotes: MutableList<Note> = mutableListOf<Note>()
    private val deletedComments: MutableList<Comment> = mutableListOf<Comment>()

    fun clear() {
        lastIdNote = 0
        lastIdComment = 0
        deletedNotes.clear()
        deletedComments.clear()
    }

    fun MutableList<Note>.indexOfFirstNoteOrNull(predicate: (Note) -> Boolean): Int? {
        val index = this.indexOfFirst(predicate)
        return if (index != -1) index else null
    }

    fun MutableList<Comment>.indexOfFirstCommentOrNull(predicate: (Comment) -> Boolean): Int? {
        val index = this.indexOfFirst(predicate)
        return if (index != -1) index else null
    }

    class NoteNotFoundException(message: String) : Exception(message)
    class CommentNotFoundException(message: String) : Exception(message)

    fun add(note: Note): Int {
        notes.add(note.copy(idNote = ++lastIdNote))
        return lastIdNote
    }

    fun createComment(comment: Comment): Int {
        val createComment = notes.find { it.idNote == comment.idNote }
            ?: throw NoteNotFoundException("Заметка с id ${comment.idNote} не найдена")
        createComment.comments.add(comment.copy(idComment = ++lastIdComment))
        return lastIdComment
    }

    fun delete(note: Note): Boolean {
        val deletedNote = notes.find { it.idNote == note.idNote }
            ?: throw NoteNotFoundException("Заметка с id ${note.idNote} не найдена")
        notes.remove(deletedNote)
        deletedNotes.add(deletedNote)
        return true
    }

        fun deleteComment(comment: Comment): Boolean {
            val findNote = notes.find { it.idNote == comment.idNote }
                ?: throw NoteNotFoundException("Заметка с id ${comment.idNote} не найдена")
            val deletedComment = findNote.comments.find { it.idComment == comment.idComment }
                ?: throw CommentNotFoundException("Комментарий с id ${comment.idComment} не найден")
            deletedComments.add(deletedComment)
            findNote.comments.remove(deletedComment)
            return true
        }

    fun edit(note: Note): Boolean {
        val indexEdit = notes.indexOfFirstNoteOrNull { it.idNote == note.idNote }
            ?: throw NoteNotFoundException("Заметка с id ${note.idNote} не найдена")
        notes[indexEdit] = note
        return true
    }

    fun editComment(comment: Comment): Boolean {
        val findNote = notes.find { it.idNote == comment.idNote }
            ?: throw NoteNotFoundException("Заметка с id ${comment.idNote} не найдена")
        val indexEdit = findNote.comments.indexOfFirstCommentOrNull { it.idComment == comment.idComment }
            ?: throw CommentNotFoundException("Комментарий с id ${comment.idComment} не найден")
        findNote.comments[indexEdit] = comment
        return true
    }

    fun get(): MutableList<Note> {
        return notes
    }

    fun getById(id: Int): Note {
        val note = notes.find { it.idNote == id } ?: throw NoteNotFoundException("Заметка с id $id не найдена")
        return note
    }

    fun getComments(note: Note): MutableList<Comment> {
        return note.comments
    }

    fun restoreComment(comment: Comment): Boolean {
        val findNote = notes.find { it.idNote == comment.idNote }
            ?: throw NoteNotFoundException("Заметка с id ${comment.idNote} не найдена или удалена, для восстановления комментария попробуте поискать заметку из удаленных")
        val findComments = deletedComments.find { it.idComment == comment.idComment }
            ?: throw CommentNotFoundException("Комментарий с id ${comment.idComment} для заметки с id ${findNote.idNote} не был найден в удаленных")
        findNote.comments.add(findComments)
        deletedComments.remove(findComments)
        return true
    }


}

fun main() {
    val note: Note = Note(0, "title", "text")
    val note2: Note = Note(0, "t", "t")

    val comment: Comment = Comment("message", 2, 0)
    println(NoteService.add(note2))
    println(NoteService.add(note))
    NoteService.notes.forEach { println(it) }
    println(NoteService.createComment(comment))
    NoteService.notes.forEach { println(it) }
}