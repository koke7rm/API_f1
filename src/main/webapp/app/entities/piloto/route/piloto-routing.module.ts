import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PilotoComponent } from '../list/piloto.component';
import { PilotoDetailComponent } from '../detail/piloto-detail.component';
import { PilotoUpdateComponent } from '../update/piloto-update.component';
import { PilotoRoutingResolveService } from './piloto-routing-resolve.service';

const pilotoRoute: Routes = [
  {
    path: '',
    component: PilotoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PilotoDetailComponent,
    resolve: {
      piloto: PilotoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PilotoUpdateComponent,
    resolve: {
      piloto: PilotoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PilotoUpdateComponent,
    resolve: {
      piloto: PilotoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pilotoRoute)],
  exports: [RouterModule],
})
export class PilotoRoutingModule {}
