import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPiloto } from '../piloto.model';
import { PilotoService } from '../service/piloto.service';
import { PilotoDeleteDialogComponent } from '../delete/piloto-delete-dialog.component';

@Component({
  selector: 'jhi-piloto',
  templateUrl: './piloto.component.html',
})
export class PilotoComponent implements OnInit {
  pilotos?: IPiloto[];
  isLoading = false;

  constructor(protected pilotoService: PilotoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pilotoService.query().subscribe(
      (res: HttpResponse<IPiloto[]>) => {
        this.isLoading = false;
        this.pilotos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPiloto): number {
    return item.id!;
  }

  delete(piloto: IPiloto): void {
    const modalRef = this.modalService.open(PilotoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.piloto = piloto;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
