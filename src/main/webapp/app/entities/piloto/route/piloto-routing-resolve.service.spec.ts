jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPiloto, Piloto } from '../piloto.model';
import { PilotoService } from '../service/piloto.service';

import { PilotoRoutingResolveService } from './piloto-routing-resolve.service';

describe('Service Tests', () => {
  describe('Piloto routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PilotoRoutingResolveService;
    let service: PilotoService;
    let resultPiloto: IPiloto | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PilotoRoutingResolveService);
      service = TestBed.inject(PilotoService);
      resultPiloto = undefined;
    });

    describe('resolve', () => {
      it('should return IPiloto returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPiloto = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPiloto).toEqual({ id: 123 });
      });

      it('should return new IPiloto if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPiloto = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPiloto).toEqual(new Piloto());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPiloto = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPiloto).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
