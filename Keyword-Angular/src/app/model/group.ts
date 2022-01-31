export class Group {
    id: string = 'group';
    groupName: string = 'group';
    avatarUrl: string = '';
    description: string = 'description';
    subscriptions: number = 0;
    isSubscribed: boolean = false;
    owner: string = '';
    moderators: string[] = [];
}