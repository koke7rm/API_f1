import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICarrera } from '../carrera.model';
import { CarreraService } from '../service/carrera.service';

@Component({
  templateUrl: './carrera-delete-dialog.component.html',
})
export class CarreraDeleteDialogComponent {
  carrera?: ICarrera;

  constructor(protected carreraService: CarreraService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.carreraService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
