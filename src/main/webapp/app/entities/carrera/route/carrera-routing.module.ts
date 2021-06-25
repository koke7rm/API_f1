import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CarreraComponent } from '../list/carrera.component';
import { CarreraDetailComponent } from '../detail/carrera-detail.component';
import { CarreraUpdateComponent } from '../update/carrera-update.component';
import { CarreraRoutingResolveService } from './carrera-routing-resolve.service';

const carreraRoute: Routes = [
  {
    path: '',
    component: CarreraComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CarreraDetailComponent,
    resolve: {
      carrera: CarreraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CarreraUpdateComponent,
    resolve: {
      carrera: CarreraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CarreraUpdateComponent,
    resolve: {
      carrera: CarreraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(carreraRoute)],
  exports: [RouterModule],
})
export class CarreraRoutingModule {}
