import pygame
import random

class Entity(pygame.sprite.Sprite):
    def __init__(self):
        super().__init__()
        self.is_alive = True

    def handle_death(self):
        self.is_alive = False
        self.kill()

class Projectile(Entity):
    def __init__(self, x, y, speed, color):
        super().__init__()
        self.image = pygame.Surface((5, 15))
        self.image.fill(color)
        self.rect = self.image.get_rect(center=(x, y))
        self.speed = speed

    def update(self):
        self.rect.y += self.speed
        if self.rect.bottom < 0 or self.rect.top > 600:
            self.handle_death()

class SpaceShip(Entity):
    def __init__(self, screen_width, screen_height, all_sprites_group):
        super().__init__()
        self.image = pygame.Surface((50, 30), pygame.SRCALPHA)
        pygame.draw.polygon(self.image, "yellow", [(25, 0), (0, 30), (50, 30)])
        self.rect = self.image.get_rect(midbottom=(screen_width / 2, screen_height - 10))
        
        self.speed = 5
        self.screen_width = screen_width
        self.projectiles = pygame.sprite.Group()
        self.all_sprites = all_sprites_group
        
        self.last_shot_time = 0
        self.shot_cooldown = 200

    def update(self):
        keys = pygame.key.get_pressed()

        if keys[pygame.K_d] or keys[pygame.K_RIGHT]:
            self.rect.x += self.speed
        if keys[pygame.K_a] or keys[pygame.K_LEFT]:
            self.rect.x -= self.speed
        
        if self.rect.right > self.screen_width:
            self.rect.right = self.screen_width
        if self.rect.left < 0:
            self.rect.left = 0

        if keys[pygame.K_SPACE]:
            self.shoot()
            
        self.projectiles.update()

    def shoot(self):
        now = pygame.time.get_ticks()
        if now - self.last_shot_time > self.shot_cooldown:
            self.last_shot_time = now
            projectile = Projectile(self.rect.centerx, self.rect.top, -10, "yellow")
            self.projectiles.add(projectile)
            self.all_sprites.add(projectile)


class Enemy(Entity):
    def __init__(self, x, y, enemy_type):
        super().__init__()
        if enemy_type == 'circle':
            self.image = pygame.Surface((30, 30), pygame.SRCALPHA)
            pygame.draw.circle(self.image, "deeppink", (15, 15), 15)
        elif enemy_type == 'rect':
            self.image = pygame.Surface((30, 30))
            self.image.fill("aquamarine")
        else:
            self.image = pygame.Surface((30, 30), pygame.SRCALPHA)
            pygame.draw.polygon(self.image, "orange", [(15, 0), (0, 30), (30, 30)])
            
        self.rect = self.image.get_rect(topleft=(x, y))
        
class Bunker(pygame.sprite.Sprite):
    def __init__(self, x, y):
        super().__init__()
        self.hit_points = 5
        
        self.original_image = pygame.Surface((80, 40))
        self.original_image.fill("green")
        
        self.image = self.original_image.copy()
        self.rect = self.image.get_rect(center=(x, y))

    def take_damage(self):
        self.hit_points -= 1
        hole_x = random.randint(5, self.rect.width - 5)
        hole_y = random.randint(5, self.rect.height - 5)
        pygame.draw.circle(self.image, "black", (hole_x, hole_y), 4)

        if self.hit_points <= 0:
            self.kill()