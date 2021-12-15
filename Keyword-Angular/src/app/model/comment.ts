export interface Comment{
    id: number;
    content: string;
    parentCommentId: number;
    user: string;
    postId: number;
    dateCreated: Date;
}