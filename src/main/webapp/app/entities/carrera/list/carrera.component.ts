import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICarrera } from '../carrera.model';
import { CarreraService } from '../service/carrera.service';
import { CarreraDeleteDialogComponent } from '../delete/carrera-delete-dialog.component';

@Component({
  selector: 'jhi-carrera',
  templateUrl: './carrera.component.html',
})
export class CarreraComponent implements OnInit {
  carreras?: ICarrera[];
  isLoading = false;

  constructor(protected carreraService: CarreraService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.carreraService.query().subscribe(
      (res: HttpResponse<ICarrera[]>) => {
        this.isLoading = false;
        this.carreras = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICarrera): number {
    return item.id!;
  }

  delete(carrera: ICarrera): void {
    const modalRef = this.modalService.open(CarreraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.carrera = carrera;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
