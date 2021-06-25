import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CarreraComponent } from './list/carrera.component';
import { CarreraDetailComponent } from './detail/carrera-detail.component';
import { CarreraUpdateComponent } from './update/carrera-update.component';
import { CarreraDeleteDialogComponent } from './delete/carrera-delete-dialog.component';
import { CarreraRoutingModule } from './route/carrera-routing.module';

@NgModule({
  imports: [SharedModule, CarreraRoutingModule],
  declarations: [CarreraComponent, CarreraDetailComponent, CarreraUpdateComponent, CarreraDeleteDialogComponent],
  entryComponents: [CarreraDeleteDialogComponent],
})
export class CarreraModule {}
