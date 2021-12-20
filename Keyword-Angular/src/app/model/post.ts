import { VoteType } from "./voteType";

export class Post {
    id: number = 0;
    groupName: string = "group";
    title: string = "title";
    description: string = "description";
    dateCreated: Date = new Date();
    username: string = "username";
    numberOfComments: number = 0;
    votes: number = 0;
    userVote: VoteType | null = null;
}