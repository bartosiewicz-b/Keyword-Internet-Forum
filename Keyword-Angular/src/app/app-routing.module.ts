import { ManageGroupComponent } from './component/manage-group/manage-group.component';
import { UserComponent } from './component/user/user.component';
import { ProfileComponent } from './component/profile/profile.component';
import { CreatePostComponent } from './component/forms/create-post/create-post.component';
import { GroupComponent } from './component/group/group.component';
import { PostFeedComponent } from './component/feed/post-feed/post-feed.component';
import { GroupFeedComponent } from './component/feed/group-feed/group-feed.component';
import { PostComponent } from './component/post/post.component';
import { LoginComponent } from './component/login/login.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './component/register/register.component';
import { CreateGroupComponent } from './component/forms/create-group/create-group.component';
import { AuthGuardService } from './service/auth-guard.service';

const routes: Routes = [
  { path: "", component: PostFeedComponent},
  { path: "login", component: LoginComponent},
  { path: "register", component: RegisterComponent},
  { path: "profile", component: ProfileComponent},
  { path: "user/:username", component: UserComponent },


  { path: "create-post", component: CreatePostComponent},
  { path: "create-post/:groupId", component: CreatePostComponent},
  { path: "create-post/:groupId/:postId", component: CreatePostComponent},
  

  { path: "groups", component: GroupFeedComponent},
  { path: "create-group", component: CreateGroupComponent, canActivate: [AuthGuardService]},
  { path: "create-group/:groupId", component: CreateGroupComponent, canActivate: [AuthGuardService]},
  { path: "manage-group/:groupId", component: ManageGroupComponent, canActivate: [AuthGuardService] },
  { path: ":groupId/:postId", component: PostComponent},
  { path: ":groupId", component: GroupComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
