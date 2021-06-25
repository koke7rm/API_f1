import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPiloto } from '../piloto.model';
import { PilotoService } from '../service/piloto.service';

@Component({
  templateUrl: './piloto-delete-dialog.component.html',
})
export class PilotoDeleteDialogComponent {
  piloto?: IPiloto;

  constructor(protected pilotoService: PilotoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pilotoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
