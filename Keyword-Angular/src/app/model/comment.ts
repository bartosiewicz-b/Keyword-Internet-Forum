import { VoteType } from './voteType';
export class Comment{
    id: number = 0;
    content: string = "content";
    parentCommentId: number | null = null;
    user: string = "username";
    postId: number = 0;
    dateCreated: Date = new Date();
    votes: number = 0;
    userVote: VoteType | null = null;
}