import { CommentService } from './service/comment.service';
import { AuthService } from './service/auth.service';
import { MemoryService } from './service/memory.service';
import { UserService } from './service/user.service';
import { ErrorInterceptorService } from './service/error-interceptor.service';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from  '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './component/login/login.component';
import { PostCardComponent } from './component/card/post-card/post-card.component';
import { AuthInterceptorService } from './service/auth-interceptor.service';
import { NavbarComponent } from './component/navbar/navbar.component';
import { PostFeedComponent } from './component/feed/post-feed/post-feed.component';
import { ExcerptPipe } from './pipe/excerpt.pipe';
import { PostComponent } from './component/post/post.component';
import { CommentCardComponent } from './component/card/comment-card/comment-card.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { GroupFeedComponent } from './component/feed/group-feed/group-feed.component';
import { GroupCardComponent } from './component/card/group-card/group-card.component';
import { GroupComponent } from './component/group/group.component';
import { FormsModule } from '@angular/forms';
import { CreatePostComponent } from './component/forms/create-post/create-post.component';
import { RegisterComponent } from './component/register/register.component';
import { CreateGroupComponent } from './component/forms/create-group/create-group.component';
import { ProfileComponent } from './component/profile/profile.component';
import { UserComponent } from './component/user/user.component';
import { ManageGroupComponent } from './component/manage-group/manage-group.component';
import { ConfirmationComponent } from './component/confirmation/confirmation.component';
import { ErrorMessageComponent } from './component/error-message/error-message.component';
import { LoadingMessageComponent } from './component/loading-message/loading-message.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    PostCardComponent,
    NavbarComponent,
    PostFeedComponent,
    ExcerptPipe,
    PostComponent,
    CommentCardComponent,
    GroupFeedComponent,
    GroupCardComponent,
    GroupComponent,
    CreatePostComponent,
    RegisterComponent,
    CreateGroupComponent,
    ProfileComponent,
    UserComponent,
    ManageGroupComponent,
    ConfirmationComponent,
    ErrorMessageComponent,
    LoadingMessageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FontAwesomeModule,
    FormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true},
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptorService, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
